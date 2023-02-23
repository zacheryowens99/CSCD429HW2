//Zachery Owens 2/21/2023 - Eastern Washington University - CSCD 429 Data Mining Homework 2
public class Gene {

    public String geneID, essential, classType, complex ,phenotype ,motif, chromosome, function, localization;
    public String[] list;
    public Gene(String line){
    list = line.split(",");
    geneID = list[0];
    essential = list[1];
    classType = list[2];
    complex = list[3];
    phenotype = list[4];
    motif = list[5];
    chromosome = list[6];
    function = list[7];
    localization = list[8];
    }
}
