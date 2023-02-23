//Zachery Owens 2/21/2023 - Eastern Washington University - CSCD 429 Data Mining Homework 2
//This is absolutely not the best program that I have written.
//There are pieces of this code that I find to be very sloppy, but it gets the job done
//The specifications of this assignment require about 40% accuracy
//I have found that my program can achieve around 50% accuracy

import java.io.*;
import java.util.*;

public class Tester {

    public static void main(String[] args) throws IOException {
        int k = 400; //k value
        ArrayList<Gene> training = ReadCSV("Genes_relation.data");
        ArrayList<Gene> testing = ReadCSV("Genes_relation.test");
        ArrayList<Boolean> accuracy = new ArrayList<Boolean>();//This keeps track of correct predictions

        //clear the output file upon runtime
        File output = new File("output.txt");
        if (output.exists())
                output.delete();


        //Each test gene
        for (Gene value : testing) {//Test every Test Gene
            //The arraylist will keep track of all similarity scores between the Test and training genes
            ArrayList<GeneSimilarity> similarities = new ArrayList<GeneSimilarity>();
            //Each training gene
            for (Gene gene : training) {//The test gene will compared to each training gene
                similarities.add(new GeneSimilarity(gene, CalculateSimilarity(gene, value)));
            }//end training gene

            //This will pick 'k' training genes with the top similarity scores
            GeneSimilarity[] gsArray = findNeighbors(similarities, k);
            //This will tally up the votes and make a prediction
            String prediction = voting(gsArray);
            //post the test geneID and prediction to output file
            printToFile(value.geneID + "," + prediction);
            //Check if the prediction is true
            accuracy.add(checkPrediction(value, prediction));
        }//end test gene

        //Output the accuracy score to the console
        System.out.println("The overall accuracy is " + performance(accuracy));
    }//end main

    //This will read the csv file and create an arraylist of Genes
    public static ArrayList<Gene> ReadCSV(String fileName) throws IOException {

        FileReader fin = new FileReader(fileName);
        BufferedReader din = new BufferedReader(fin);
        ArrayList<Gene> list = new ArrayList<Gene>();
        din.readLine();//Remove the top line

        String thisLine;
        while ((thisLine = din.readLine()) != null) {//iterate through all lines
            list.add(new Gene(thisLine));//add a new Gene to the arraylist
        }
        fin.close();
        din.close();
        return list;
    }

    //This method will append output file with a given string line
    public static void printToFile(String line) throws IOException {
        FileWriter writer = new FileWriter("output.txt", true);
        BufferedWriter buff = new BufferedWriter(writer);
        buff.write(line);
        buff.newLine();
        buff.close();
    }

    //This method will calculate the cosine similarity between two genes
    //Dot Product: matching attributes result in a value of one, then the number of all matching attributes will added together
    //Attributes with missing values ('?') will be ignored
    public static double CalculateSimilarity(Gene g1, Gene g2) {
        int dotProduct = 0;
        double magnitudeG1 = 0, magnitudeG2 = 0;
        //compare each
        for(int i = 0; i < 8; i++){
            if(g1.list[i].equals(g2.list[i]) && g1.list[i] != "?")
                dotProduct++;
        }

        //magnitudes
        magnitudeG1 = CalculateMagnitude(g1);
        magnitudeG2 = CalculateMagnitude(g2);


        return (double) dotProduct/(magnitudeG1 * magnitudeG2);
    }//end calcSimilarity

    //Magnitude: The square root of the number of existing attributes in the gene entry
    //Attributes with missing values ('?') will be ignored
    public static double CalculateMagnitude(Gene g) {
        double magnitude = 0;
        for (int i = 0; i < g.list.length; i++) {
            if (!(g.list[i].equals("?")))
                magnitude++;
        }
        magnitude = Math.sqrt(magnitude);
        return magnitude;
    }//end CalcMagnitude

    //This method will check the prediction made by voting() and return if the prediction is true
    //The prediction is checked by finding the geneID of the test gene inside the file keys.txt and comparing the prediction to the actual value
    public static boolean checkPrediction(Gene test, String prediction) throws IOException {
        FileReader reader = new FileReader("keys.txt");
        BufferedReader buff = new BufferedReader(reader);

        String line;
        while((line = buff.readLine()) != null) {
            String[] splitLine = line.split(",");

            if (test.geneID.equals(splitLine[0]))
                if (prediction.equals(splitLine[1]))
                    return true;

        }
        return false;
    }//end checkPrediction

    //performance will take the boolean ArrayList and count all the true values
    //The number of true values is divided by the size of the ArrayList to give the user an accuracy score
    public static double performance(ArrayList<Boolean> accuracy){
        int trueCount = 0;

        for(boolean value: accuracy){
            if(value)
                trueCount++;
        }//end preformance

        return (double) trueCount / accuracy.size() ;
    }//end performance

    //findNeighbors will take the top 'k' elements with the highest similarity scores
    //The top 'k' elements are returned in an array.
    public static GeneSimilarity[] findNeighbors(ArrayList<GeneSimilarity> gs, int k){

        GeneSimilarity[] array = new GeneSimilarity[k];

        Comparator<GeneSimilarity> desending = new Comparator<GeneSimilarity>() {
            @Override
            public int compare(GeneSimilarity o1, GeneSimilarity o2) {
                return Double.compare(o2.similarity, o1.similarity);
            }
        };
        Collections.sort(gs, desending);

        for(int i = 0; i < array.length; i++)
            array[i] = gs.get(i);

        return array;
    }//end findNeighbors

    //This method will take an array of the top k elements and return a prediction of localization
    //This will count the localization attributes of the training genes inside the array
    //Then will use a majority vote to make a prediction of the localization attribute of the test gene
    public static String voting(GeneSimilarity[] gs){
        int votes[] = new int[15];

        for(GeneSimilarity vote: gs){
            switch (vote.trainGene.localization) {
                case ("cell wall") -> votes[0]++;
                case ("cytoplasm") -> votes[1]++;
                case("cytoskeleton")->votes[2]++;
                case("endosome")->votes[3]++;
                case("ER")->votes[4]++;
                case("extracellular")->votes[5]++;
                case("golgi")->votes[6]++;
                case("integral membrane")->votes[7]++;
                case("lipid particles")->votes[8]++;
                case("mitochondria")->votes[9]++;
                case("nucleus")->votes[10]++;
                case("peroxisome")->votes[11]++;
                case("plasma membrane")->votes[12]++;
                case("transport vesicles")->votes[13]++;
                case("vacuole")->votes[14]++;
            }//end switch
        }//end for
        //we need to return the localization with the most votes
        int topVotes = 0;
        int greatestIndex = 0;

        for(int i = 0; i < votes.length; i++)
            if (votes[i] > topVotes) {
                topVotes = votes[i];
                greatestIndex = i;
            }
        if (greatestIndex == 0)
            return "cell wall";
        else if(greatestIndex == 1)
            return "cytoplasm";
        else if(greatestIndex == 2)
            return "cytoskeleton";
        else if (greatestIndex == 3)
            return "endosome";
        else if (greatestIndex == 4)
            return "ER";
        else if (greatestIndex == 5)
            return "extracellular";
        else if (greatestIndex == 6)
            return "golgi";
        else if(greatestIndex == 7)
            return "integral membrane";
        else if (greatestIndex == 8)
            return "lipid particles";
        else if (greatestIndex == 9)
            return "mitochondria";
        else if (greatestIndex == 10)
            return "nucleus";
        else if (greatestIndex == 11)
            return "peroxisome";
        else if (greatestIndex == 12)
            return "plasma membrane";
        else if (greatestIndex == 13)
            return "transport vesicles";
        else
            return "vacuole";

    }//end voting
}//end tester
