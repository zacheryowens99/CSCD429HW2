Intro
Tasked with creating a classifier method to predict the attribute "localization" inside a test set of data.
A training set is provided to create a prediction model, but the training data is missing a signifigant amout of values
For my program, I have decided to use the KNN approach in order to predict the localization attribute 
inside the test file.

How to run the program

The program requires no parameters to run if all gene files are contained inside the same src folder as 
the program, then it should run correctly. The main method that operates this program is contained in 
tester.java. If you would like to adjust the k value, that can be done by editing the variable k inside 
main().

From the Windows PowerShell:
cd into /src
javac tester.java
java tester.java

Handling missing data

There is lots of missing data inside both the training and testing set. Some attruibutes are missing 50-70% of values. 
If my program encountered a missing piece of data, it ignored the attribute.

Method used to build the classifier

As stated above, the classifier used was K Nearest Neighbor. Here’s a breakdown of how I structured my 
prediction model:
1. Each test gene is compared to all training genes
2. To find the similarity between the test gene and training gene, I used a Cosine Similarity. To find the 
dot product between the two genes, I took the sum of the number of matching attributes. To find the 
magnitude of each gene, I took the square root of the number of non-missing attributes.
3. With that I had a list of similarity scores between the test gene and every training gene. The program
picked the top ‘k’ training genes with the highest similarity scores and set those aside for voting.
4. With the list of k nearest neighbors, the program counted the amount of each localization attribute 
that appears in training genes
5. The localization value that appears the most among the k nearest neighbors list will be the prediction 
for localization in that test gene.
6. Repeat the process with each testing gene


Accuracy

Using a k value = 400, the program yielded an overall accuracy of 53.65%

The accuracy of my model is compared to the file keys.txt. Each geneID from the test is compared
to the geneID from keys.txt, the ammount correct predictions is divided by the total number of test tuples.
If I could go back I would probably find what attributes are more correlated to localization and give them a greater
weight when calculating the similarity score.