package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
        System.out.println(problem.getName());

        if(problem.hasVerbal()){
                HashMap<String, RavensFigure> figures=new HashMap<>();
        figures=problem.getFigures();
        
        Set<String> figuresNames=figures.keySet();

        

        HashMap<String,String> transformation1=findTransformation(figures.get("A"),figures.get("B"));
        HashMap<String,String> transformation2=findTransformation(figures.get("A"),figures.get("C"));
        HashMap<String,String> probableSolution1=applyTransformation(figures.get("C"),transformation1);
        HashMap<String,String> probableSolution2=applyTransformation(figures.get("B"),transformation2);
        String numRegex="^\\d+$";
        int ansRating=0;
        String ansOption="-1";
        for(String figureName: figuresNames){
            

            if(figureName.matches(numRegex)){
                
                RavensFigure option =figures.get(figureName);
                int thisOptionRating=compareProbables(probableSolution1,option);
                
                if(thisOptionRating>ansRating){
                    ansRating=thisOptionRating;
                    ansOption=figureName;
                }


            }
        

        }
        
        if(ansRating>9){
        System.out.println("Answer from Agent : "+ansOption);    
            return Integer.parseInt(ansOption);
        }else{
                String numRegex1="^\\d+$";
                int ansRating1=0;
                String ansOption1="-1";
                for(String figureName: figuresNames){
                    

                    if(figureName.matches(numRegex1)){
                        
                        RavensFigure option =figures.get(figureName);
                        int thisOptionRating=compareProbables(probableSolution2,option);
                        
                        if(thisOptionRating>ansRating){
                            ansRating1=thisOptionRating;
                            ansOption1=figureName;
                        }


                    }
                

                }
             if(ansRating1>9){
               System.out.println("Answer from Agent : "+ansOption1);    
                return Integer.parseInt(ansOption1);
             }else{
                return -1;
             }   

        }
            
        }else{
            return -1;
        }
        

        
    }

    public HashMap<String,String> findTransformation(RavensFigure A, RavensFigure B){
        
        
        HashMap<String,RavensObject> valuesOfA=A.getObjects();
        HashMap<String,RavensObject> valuesOfB=B.getObjects();
        HashMap<String,String> transformation=new HashMap<>();
        Set<String> typesOfValuesA=valuesOfA.keySet();
        Set<String> typesOfValuesB=valuesOfB.keySet();
        int noOfKeys=typesOfValuesA.size();
        

            for(int i=0;i<noOfKeys;i++){
            
            HashMap<String,String> attributesofA=valuesOfA.get(typesOfValuesA.toArray()[i]).getAttributes();
            HashMap<String,String> attributesofB=valuesOfB.get(typesOfValuesB.toArray()[i]).getAttributes();

            Set<String> attributesofANames=attributesofA.keySet();
            
            for(String a: attributesofANames){
                if((attributesofA.get(a)!=null)&&(attributesofB.get(a)!=null))
                    if((a.equalsIgnoreCase("inside"))||(a.equalsIgnoreCase("above"))){
                        
                        if(i>0)
                            transformation.put(a+i,identifyTransformation(a,attributesofA.get("shape")+"-"+valuesOfA.get(attributesofA.get(a)).getAttributes().get("shape"),attributesofB.get("shape")+"-"+valuesOfB.get(attributesofB.get(a)).getAttributes().get("shape")));
                        else
                            transformation.put(a,identifyTransformation(a,attributesofA.get("shape")+"-"+valuesOfA.get(attributesofA.get(a)).getAttributes().get("shape"),attributesofB.get("shape")+"-"+valuesOfB.get(attributesofB.get(a)).getAttributes().get("shape")));
                    }else{
                        if(i>0)
                            transformation.put(a+i,identifyTransformation(a,attributesofA.get(a),attributesofB.get(a)));
                        else
                            transformation.put(a,identifyTransformation(a,attributesofA.get(a),attributesofB.get(a)));    
                    }
                    
                        
                }
            
            }    

        
        
        
        
        System.out.println("Transformation :  "+transformation);
        return transformation;
    }

    public String identifyTransformation(String attributeName,String attributeValue1,String attributeValue2){
        if(attributeName.equalsIgnoreCase("angle")){
            return String.valueOf(Math.abs(Integer.parseInt(attributeValue1)-Integer.parseInt(attributeValue2)));

        }else if(attributeName.equalsIgnoreCase("shape")){
            if(attributeValue1.equalsIgnoreCase(attributeValue2))
                return "nochange";
            else
                return attributeValue1+","+attributeValue2;
        }else if(attributeName.equalsIgnoreCase("alignment")){
            if(attributeValue1.equalsIgnoreCase(attributeValue2))
                return "nochange";
            else
                return attributeValue1+","+attributeValue2;
        }else if(attributeName.equalsIgnoreCase("fill")){
            if(attributeValue1.equalsIgnoreCase(attributeValue2))
                return "nochange";
            else
                return attributeValue1+","+attributeValue2;
        }else{
            if(attributeValue1.equalsIgnoreCase(attributeValue2))
                return "nochange";
            else
                return attributeValue1+","+attributeValue2;    
        }
        
    }

    public HashMap<String,String> applyTransformation(RavensFigure C,HashMap<String,String> transformation){
        HashMap<String,String> probableSolution=new HashMap<>();
        HashMap<String,RavensObject> valuesOfC=C.getObjects();
        Set<String> typesOfValuesC=valuesOfC.keySet();
        int noOfKeys=typesOfValuesC.size();
        
        for(int i=0;i<noOfKeys;i++){
            HashMap<String,String> attributesofC=valuesOfC.get(typesOfValuesC.toArray()[i]).getAttributes();
            Set<String> attributesofCNames=attributesofC.keySet();
            for(String a: attributesofCNames){
                
                if((attributesofC.get(a)!=null)&&(transformation.get(a+((i>0)?i:""))!=null)){
                    if(a.equalsIgnoreCase("angle")){
                        
                        probableSolution.put(a+((i>0)?i:""),String.valueOf(Integer.parseInt(attributesofC.get(a))-Integer.parseInt(transformation.get(a+((i>0)?i:"")))));
                        

                     }else if(a.equalsIgnoreCase("shape")){
                        if(transformation.get(a+((i>0)?i:"")).equalsIgnoreCase("nochange")){
                            probableSolution.put(a+((i>0)?i:""),attributesofC.get(a));    
                         }else{

                            if(transformation.get(a+((i>0)?i:"")).split(",")[0].equalsIgnoreCase(attributesofC.get(a))){
                                 probableSolution.put(a+((i>0)?i:""),transformation.get(a).split(",")[1]);
                            }
                         }

                     }else if(a.equalsIgnoreCase("alignment")){
                        if(transformation.get(a+((i>0)?i:"")).equalsIgnoreCase("nochange")){
                            probableSolution.put(a+((i>0)?i:""),attributesofC.get(a));    
                         }else{
                            probableSolution.put(a+((i>0)?i:""),findtheAlignment(transformation.get(a+((i>0)?i:"")).split(",")[0],transformation.get(a+((i>0)?i:"")).split(",")[1],attributesofC.get(a)));
                            
                         }

                     }else if(a.equalsIgnoreCase("fill")){
                        if(transformation.get(a+((i>0)?i:"")).equalsIgnoreCase("nochange")){
                            probableSolution.put(a+((i>0)?i:""),attributesofC.get(a));    
                         }else{
                            probableSolution.put(a+((i>0)?i:""),findtheFill(transformation.get(a+((i>0)?i:"")).split(",")[0],transformation.get(a+((i>0)?i:"")).split(",")[1],attributesofC.get(a)));
                            
                         }

                     }else if((a.equalsIgnoreCase("inside"))||(a.equalsIgnoreCase("above"))){
                        if(transformation.get(a+((i>0)?i:"")).equalsIgnoreCase("nochange")){
                            probableSolution.put(a+((i>0)?i:""),valuesOfC.get(attributesofC.get(a)).getAttributes().get("shape"));    
                         }else{
                            probableSolution.put(a+((i>0)?i:""),findtheInside(C,transformation.get(a+((i>0)?i:"")).split(",")[0],transformation.get(a+((i>0)?i:"")).split(",")[1],attributesofC.get(a)));
                            
                         }

                     }else{
                        if(transformation.get(a+((i>0)?i:"")).equalsIgnoreCase("nochange")){
                        
                            probableSolution.put(a+((i>0)?i:""),attributesofC.get(a));
                        }    

                    }
                
                }
                
                    
            }
        }
        System.out.println("Probable Solution:  "+probableSolution);
        return probableSolution;
    }

    public int compareProbables(HashMap<String,String> probableSolution,RavensFigure option){
        int tempRating=0,noOfAttributes=0;;
        HashMap<String,RavensObject> valueOfOption=option.getObjects();
        Set<String> typesOfValuesOption=valueOfOption.keySet();
        int noOfKeys=typesOfValuesOption.size();
        int totalnoOfAttributes=0;
        
        for(int i=noOfKeys-1;i>=0;i--){
            HashMap<String,String> attributesofOption=valueOfOption.get(typesOfValuesOption.toArray()[i]).getAttributes();
            Set<String> attributesofOptionNames=attributesofOption.keySet();
            noOfAttributes=attributesofOptionNames.size();
            totalnoOfAttributes=totalnoOfAttributes+noOfAttributes;
            
            for(String a: attributesofOptionNames){

                
                if((attributesofOption.get(a)!=null)&&(probableSolution.get(a+(((noOfKeys-(i+1))>0)?(noOfKeys-(i+1)):""))!=null)){
                    // System.out.println(a+" #### "+probableSolution.get(a+((i>0)?i:""))+" ----> "+attributesofOption.get(a));
                    if((a.equalsIgnoreCase("inside"))||(a.equalsIgnoreCase("above"))){
                        if(probableSolution.get(a+(((noOfKeys-(i+1))>0)?(noOfKeys-(i+1)):"")).equalsIgnoreCase(valueOfOption.get(attributesofOption.get(a)).getAttributes().get("shape"))){
                            tempRating++;
                        }          
                    }else{
                        if(probableSolution.get(a+(((noOfKeys-(i+1))>0)?(noOfKeys-(i+1)):"")).equalsIgnoreCase(attributesofOption.get(a))){
                        //System.out.println(a);
                            tempRating++;
                        }    
                    }
                    
                 }   

            }
        }
        
        return (tempRating/totalnoOfAttributes)*10;

    }

    public String findtheAlignment(String attribute1, String attribute2,String attribute3){
        String[] attribute1Array=attribute1.split("-");
        String[] attribute2Array=attribute2.split("-");
        String[] attribute3Array=attribute3.split("-");
        HashMap<String,String> alignmentLearning=new HashMap<>();
        String outputAlignment="";

        for (int i=0;i<attribute1Array.length; i++ ) {
            if(attribute1Array[i].equalsIgnoreCase("bottom")){
                if(attribute1Array[i].equalsIgnoreCase(attribute2Array[i])){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("top","top");
                }else if(attribute2Array[i].equalsIgnoreCase("top")){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("top","bottom");
                }
            }else if(attribute1Array[i].equalsIgnoreCase("top")){
                if(attribute1Array[i].equalsIgnoreCase(attribute2Array[i])){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("bottom","bottom");
                }else if(attribute2Array[i].equalsIgnoreCase("bottom")){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("bottom","top");
                }
            }else if(attribute1Array[i].equalsIgnoreCase("right")){
                if(attribute1Array[i].equalsIgnoreCase(attribute2Array[i])){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("left","left");
                }else if(attribute2Array[i].equalsIgnoreCase("left")){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("left","right");
                }
            }else if(attribute1Array[i].equalsIgnoreCase("left")){
                if(attribute1Array[i].equalsIgnoreCase(attribute2Array[i])){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("right","right");
                }else if(attribute2Array[i].equalsIgnoreCase("right")){
                    alignmentLearning.put(attribute1Array[i],attribute2Array[i]);
                    alignmentLearning.put("right","left");
                }
            }
            
        }

        for(int j=0;j<attribute3Array.length;j++){
            outputAlignment=outputAlignment+((j>0)?"-":"")+alignmentLearning.get(attribute3Array[j]);
        }

        return outputAlignment;
    }

    public String findtheInside(RavensFigure C,String attribute1,String attribute2,String attribute3){
        
        HashMap<String,RavensObject> valueOfC=C.getObjects();
        String suggestedFigureOption="";
        
        String suggestedFigure="";
        if(attribute3.equalsIgnoreCase(attribute2))
            suggestedFigure=attribute1;
        else
            suggestedFigure=attribute2;
        Set<String> typesofCValues=valueOfC.keySet();
        for(String a: typesofCValues){
            if((valueOfC.get(a).getAttributes().get("shape")).equalsIgnoreCase(suggestedFigure))
                suggestedFigureOption=a;
        }

        return suggestedFigureOption;
    }

    public String findtheFill(String attribute1,String attribute2,String attribute3){
        if(attribute1.equalsIgnoreCase(attribute3)){
            return attribute2;

        }else{
            if(attribute2.equalsIgnoreCase("no"))
                return "yes";
            else
                return "no";
        }
    }

    public String testFunction(String message){
        return message;
    }
}