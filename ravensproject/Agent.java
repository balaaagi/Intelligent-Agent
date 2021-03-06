package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.DecimalFormat;
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
        int answer=-1;
        System.out.println(problem.getName());
        if(problem.getProblemType().equalsIgnoreCase("2x2")){
            if(problem.hasVerbal()){
                HashMap<String, RavensFigure> figures=new HashMap<>();
        figures=problem.getFigures();
        
        Set<String> figuresNames=figures.keySet();

        System.out.println(figures.size());

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
        }else{
            //3x3
            try{
              if(problem.hasVisual()){
                HashMap<String, RavensFigure> problemFigures=problem.getFigures();
                HashMap<String,Integer> problemInPixels=findthePixels(problemFigures);
                HashMap<String,String> problemTransformation=findtheTransformation(problemInPixels);
                HashMap<String,Integer[][]> problemInPixelMap=findPixelMap(problemFigures);
               answer=Integer.parseInt(findtheAnswer(problemInPixels,problemTransformation,problemFigures,problemInPixelMap));
                
              System.out.println("Visual is available");

              }else{
                System.out.println("No Visual Available");
              }

            }catch(Exception e){
              e.printStackTrace();
            }
            System.out.println("Answer from Agent : "+answer); 
            return answer;
        
            
        }
        
        

        
    }

    public HashMap<String,Integer> findthePixels(HashMap<String,RavensFigure> problemImages) {
      HashMap<String,Integer> thisOptionInPixels=new HashMap<String,Integer>();
      Set<String> optionImageNames=problemImages.keySet();
      try{
      for(String option: optionImageNames){
        RavensFigure thisFigure=problemImages.get(option);
        BufferedImage thisOptionImage=ImageIO.read(new File(thisFigure.getVisual()));
        int pixelCount=0;
        for(int i=0;i<thisOptionImage.getHeight();i++){
          for(int j=0;j<thisOptionImage.getWidth();j++){
              if(thisOptionImage.getRGB(i,j)==-16777216)
                pixelCount++;
          }
        }
        thisOptionInPixels.put(option,pixelCount);
      }
    }catch(Exception e){
                    e.printStackTrace();
    }
      return thisOptionInPixels;
    }


 public HashMap<String,String> findtheTransformation(HashMap<String,Integer> problemInPixels){
   HashMap<String,String> transformation=new HashMap<String,String>();
   Set<String> optionNames=problemInPixels.keySet();

  String abTransition=findTransistion(problemInPixels.get("A"),problemInPixels.get("B"));
  String bcTransition=findTransistion(problemInPixels.get("B"),problemInPixels.get("C"));
  String deTransition=findTransistion(problemInPixels.get("D"),problemInPixels.get("E"));
  String efTransition=findTransistion(problemInPixels.get("E"),problemInPixels.get("F"));
  String ghTransition=findTransistion(problemInPixels.get("G"),problemInPixels.get("H"));


  if((abTransition==bcTransition)&&(deTransition==efTransition)){
        transformation.put("pixelChange","no-change");
        transformation.put("transitionChange","no-change");  
  }
 else{
   if((abTransition==deTransition)&&(bcTransition==efTransition)){
     transformation.put("pixelChange","column");
     transformation.put("transitionChange","column");
   }else{

     transformation.put("transitionChange","cyclic");
     transformation.put("pixelChange","cyclic");
   }

 }

   return transformation;
 }

 public String findTransistion(int a, int b){
   String transition="";
  if((a==b))
    transition="no-change";

  if((a>b))
    transition="decrement";

  if((b>a))
    transition="increment";

    return transition;
 }

 public String findtheAnswer(HashMap<String,Integer> problemInPixels,HashMap<String,String> transformation,HashMap<String,RavensFigure> problemFigures,HashMap<String,Integer[][]> problemPixelMap ){
   String theAnswer="";
   int typeOfProblem=-1;
   typeOfProblem=findProblemPattern(problemPixelMap);
   
   if(transformation.get("pixelChange").equalsIgnoreCase("no-change")){
              System.out.println("This Problem is Constant");
              theAnswer=findAnswerConstant(problemInPixels.get("G"),problemInPixels);
            
   }else if(transformation.get("transitionChange").equalsIgnoreCase("column")){
              System.out.println("This Problem is Column Wise");
            if(typeOfProblem==-1)
              theAnswer=findAnswerColumn(problemInPixels,problemFigures,problemPixelMap);
            else
              theAnswer=findAnswerCyclicPattern(typeOfProblem,problemPixelMap,problemInPixels);
   }else{
              System.out.println("This Problem is Cyclic ");
          
          if(typeOfProblem==-1)
            theAnswer=findAnswerCyclic(problemInPixels,problemPixelMap);
          else
            theAnswer=findAnswerCyclicPattern(typeOfProblem,problemPixelMap,problemInPixels);
   }
   if(theAnswer.equalsIgnoreCase("")){
      theAnswer=findAnswerCyclicPattern(typeOfProblem,problemPixelMap,problemInPixels);      
   }
   return theAnswer;
 }

 

 public String findAnswerConstant(int a , HashMap<String,Integer> problemInPixels){
  String answer="";
  String numRegex="^\\d+$";
  HashMap<String,Double> answers=new HashMap<String,Double>();
  Set<String> problem=problemInPixels.keySet();
  for(String option:problem){
    if(option.matches(numRegex)){
      double accuracyPercentage=(Math.abs(problemInPixels.get(option)-a)/(double)((a>problemInPixels.get(option))?problemInPixels.get(option):a));
      if(problemInPixels.get(option)==a)
        answer=option;
      else{
        if(accuracyPercentage<0.5)
          answers.put(option,accuracyPercentage);
      }

    }
  }
  if(answer.equalsIgnoreCase("")){
      Set<String> accuracyOptions=answers.keySet();
      double nearestAccuracy=0.5;

      for(String s: accuracyOptions){
        if(answers.get(s)<nearestAccuracy){
            nearestAccuracy=answers.get(s);
            answer=s;
        }
      }


  }
return answer;
 }

 public String findAnswerCyclicPattern(int pattern,HashMap<String,Integer[][]> problemPixelMap,HashMap<String,Integer> problemInPixels){
  String theAnswer="";
  String numRegex="^\\d+$";
  Set<String> optionNames=problemPixelMap.keySet();
  Integer[][] probableAnswer=null;
  double answerAccuracy=0.0;
  if(pattern==1){
    probableAnswer=mergeTwoOptions(problemPixelMap.get("G"),problemPixelMap.get("H"));
  }else if(pattern==2){
    probableAnswer=mergeTwoOptions(problemPixelMap.get("C"),problemPixelMap.get("F"));
  }else if(pattern==3){
    probableAnswer=diffTwoOptions(problemPixelMap.get("C"),problemPixelMap.get("F"));
  }else if(pattern==4){
    probableAnswer=diffTwoOptions(problemPixelMap.get("G"),problemPixelMap.get("H"));
  }else if(pattern==5){
    probableAnswer=diffTwoOptions(problemPixelMap.get("H"),problemPixelMap.get("G"));
  }else if(pattern==6){
    probableAnswer=interesctTwoOptions(problemPixelMap.get("G"),problemPixelMap.get("H"));
  }
  
  for(String optionName:optionNames){
    if(optionName.matches(numRegex)){
          double probableAnswerAccuracyWithOption=compareObjects(probableAnswer,problemPixelMap.get(optionName));
          if(probableAnswerAccuracyWithOption>answerAccuracy){
            answerAccuracy=probableAnswerAccuracyWithOption;
            theAnswer=optionName;
            
          }
          
    
    }
      
  }
  
  return theAnswer;
 }

 public int findProblemPattern(HashMap<String,Integer[][]> problemPixelMap){
  int pattern=-1;
  String numRegex="^\\d+$";
  Set<String> optionNames=problemPixelMap.keySet();
  Integer[][] mergeAB=mergeTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("B"));
  Integer[][] mergeDE=mergeTwoOptions(problemPixelMap.get("D"),problemPixelMap.get("E"));
  double accuracyPercentageAB=compareObjects(mergeAB,problemPixelMap.get("C"));
  double accuracyPercentageDE=compareObjects(mergeDE,problemPixelMap.get("F"));
  if(((accuracyPercentageAB>0.99)&&(accuracyPercentageAB<=1.0))&&((accuracyPercentageDE>0.9)&&(accuracyPercentageDE<=1.0))){
    System.out.println("This Problem is of type  Horizontal Sum ");
    pattern=1;
    
  }else{
    Integer[][] mergeAD=mergeTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("D"));
    Integer[][] mergeBE=mergeTwoOptions(problemPixelMap.get("B"),problemPixelMap.get("E"));
    double accuracyPercentageAD=compareObjects(mergeAB,problemPixelMap.get("G"));
    double accuracyPercentageBE=compareObjects(mergeBE,problemPixelMap.get("H"));

    if(((accuracyPercentageAD>0.99)&&(accuracyPercentageAD<=1.0))&&((accuracyPercentageBE>0.99)&&(accuracyPercentageBE<=1.0))){
        System.out.println("The problem is vertical Sum");
      pattern=2;
      
    }
    else{
      Integer[][] diffAD=diffTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("D"));
      Integer[][] diffBE=diffTwoOptions(problemPixelMap.get("B"),problemPixelMap.get("E"));
      double accuracyPercentageADDiff=compareObjects(diffAD,problemPixelMap.get("G"));
      double accuracyPercentageBEDiff=compareObjects(diffBE,problemPixelMap.get("H"));

      if(((accuracyPercentageADDiff>0.9)&&(accuracyPercentageADDiff<=1.0))&&((accuracyPercentageBEDiff>0.9)&&(accuracyPercentageBEDiff<=1.0))){
        System.out.println("The problem is vertical Difference");
        
        pattern=3;  
      }else{
        Integer[][] diffAB=diffTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("B"));
        Integer[][] diffDE=diffTwoOptions(problemPixelMap.get("D"),problemPixelMap.get("E"));
        double accuracyPercentageABDiff=compareObjects(diffAB,problemPixelMap.get("C"));
        double accuracyPercentageDEDiff=compareObjects(diffDE,problemPixelMap.get("F"));
        if(((accuracyPercentageABDiff>0.9)&&(accuracyPercentageABDiff<=1.0))&&((accuracyPercentageDEDiff>0.9)&&(accuracyPercentageDEDiff<=1.0))){
          System.out.println("The problem is Horizontal Difference");
          pattern=4;
        }else{
        Integer[][] diffBA=diffTwoOptions(problemPixelMap.get("D"),problemPixelMap.get("A"));
        Integer[][] diffED=diffTwoOptions(problemPixelMap.get("E"),problemPixelMap.get("B"));
        double accuracyPercentageBADiff=compareObjects(diffBA,problemPixelMap.get("G"));
        double accuracyPercentageEDDiff=compareObjects(diffED,problemPixelMap.get("H"));
        if(((accuracyPercentageBADiff>0.9)&&(accuracyPercentageBADiff<=1.0))&&((accuracyPercentageEDDiff>0.9)&&(accuracyPercentageEDDiff<=1.0))){
         System.out.println("The problem is Horizontal Difference Reverse");
         pattern=5;
        }
        else{
          Integer[][] intersectAB=interesctTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("B"));
          Integer[][] intersectDE=interesctTwoOptions(problemPixelMap.get("D"),problemPixelMap.get("E"));
          double accuracyPercentageABInter=compareObjects(intersectAB,problemPixelMap.get("C"));
          double accuracyPercentageDEInter=compareObjects(intersectDE,problemPixelMap.get("F"));
          if(((accuracyPercentageABInter>0.99)&&(accuracyPercentageABInter<=1.0))&&((accuracyPercentageDEInter>0.99)&&(accuracyPercentageDEInter<=1.0))){
              System.out.println("The problem is Horizontal Intersection"); 
              pattern=6; 
            }
          else{
            Integer[][] intersectAD=interesctTwoOptions(problemPixelMap.get("A"),problemPixelMap.get("D"));
            Integer[][] intersectBE=interesctTwoOptions(problemPixelMap.get("B"),problemPixelMap.get("E"));
            double accuracyPercentageADInter=compareObjects(intersectAD,problemPixelMap.get("G"));
            double accuracyPercentageBEInter=compareObjects(intersectBE,problemPixelMap.get("H"));
            if(((accuracyPercentageADInter>0.99)&&(accuracyPercentageADInter<=1.0))&&((accuracyPercentageBEInter>0.99)&&(accuracyPercentageBEInter<=1.0))){
              System.out.println("The problem is Vertical Intersection"); 
              pattern=7; 
            }else{
              System.out.println("The problem Pattern Cannot be found"); 
              
            }
          
          
        }
       }
        }

      }
      
    }
  }
    
  

  return pattern;

 }

 public String findAnswerCyclic(HashMap<String,Integer> problemInPixels,HashMap<String,Integer[][]> problemPixelMap){
   String theAnswer="";
   String numRegex="^\\d+$";
   Set<String> figureNames=problemInPixels.keySet();
   HashMap<String,Double> answers=new HashMap<String,Double>();
   HashMap<Integer,Integer> pixelsCyclic=new HashMap<Integer,Integer>();
   int aPixels=problemInPixels.get("A");
   int ePixels=problemInPixels.get("E");
   int cPixels=problemInPixels.get("C");
   int gPixels=problemInPixels.get("G");

   
     for(String figureName:figureNames){
            Integer optionPixels=problemInPixels.get(figureName);

            if(figureName.matches(numRegex)){

            }else{
             // System.out.println("Option "+ figureName+"Pixel "+optionPixels);
              if(pixelsCyclic.containsKey(optionPixels)){

                 int newValue=pixelsCyclic.get(optionPixels);
                 newValue=newValue+1;

                  pixelsCyclic.put(optionPixels,newValue);
              }else{

                pixelsCyclic.put(optionPixels,1);
              }
            }
      }
  Set<Integer> pixelsCyclicValues=pixelsCyclic.keySet();
  int expectedAnswerPixels=-1;
  for(Integer a: pixelsCyclicValues){

    if(pixelsCyclic.get(a)<3){
      expectedAnswerPixels=a;
      break;
    }
  }
boolean diagonalPercentageTrack=false;
// System.out.println(expectedAnswerPixels);
  if(expectedAnswerPixels!=-1){
    double diagonalNearest1=(Math.abs(aPixels-expectedAnswerPixels))/(double)((aPixels>expectedAnswerPixels)?expectedAnswerPixels:aPixels)*100;
    double diagonalNearest2=(Math.abs(ePixels-expectedAnswerPixels))/(double)((ePixels>expectedAnswerPixels)?expectedAnswerPixels:ePixels)*100;
    double diagonalPercentage1=(Math.abs(aPixels-ePixels))/(double)((aPixels>ePixels)?ePixels:aPixels);
    double diagonalPercentage2=(Math.abs(cPixels-ePixels))/(double)((cPixels>ePixels)?ePixels:cPixels);
    double diagonalPercentage3=(Math.abs(gPixels-ePixels))/(double)((gPixels>ePixels)?ePixels:gPixels);
    // System.out.println(diagonalNearest1);
    // System.out.println(diagonalNearest2);
    // System.out.println(diagonalPercentage1);
    // System.out.println(diagonalPercentage2);
    // System.out.println(diagonalPercentage3);
    double nearestAccuracyStrengthMap=0.0;
    double nearestAccuracy=0.5;
    for (String optionName : figureNames) {

      if(optionName.matches(numRegex)){

        if((diagonalNearest1>0.5)||(diagonalNearest2>0.5)){
          
            double accuracyPercentage=(Math.abs(ePixels-problemInPixels.get(optionName)))/(double)((ePixels>problemInPixels.get(optionName))?problemInPixels.get(optionName):ePixels);
            double ansDiagonalCompareAccuracy=(Math.abs(accuracyPercentage-diagonalPercentage1))/(double)((diagonalPercentage1>accuracyPercentage)?accuracyPercentage:diagonalPercentage1);

            // System.out.println("option "+optionName+" Pixel Values "+problemInPixels.get(optionName)+" "+accuracyPercentage+ "Answer Accyracy"+ansDiagonalCompareAccuracy );
            if(ansDiagonalCompareAccuracy==0.0){
              theAnswer=optionName;
            }else if(ansDiagonalCompareAccuracy<1){
              // System.out.println("Coming in Pixel Strength Mapping");
              if(ansDiagonalCompareAccuracy>nearestAccuracyStrengthMap){
                  nearestAccuracyStrengthMap=ansDiagonalCompareAccuracy;
                  theAnswer=optionName;
            }

                
              // answers.put(optionName,ansDiagonalCompareAccuracy);

            }

              
        }else{
          // System.out.println("Coming in Pixel Normal Flow");
              double accuracyPercentage=(Math.abs(problemInPixels.get(optionName)-expectedAnswerPixels)/
              (double)((expectedAnswerPixels>problemInPixels.get(optionName))?problemInPixels.get(optionName):expectedAnswerPixels));


            
              if(problemInPixels.get(optionName)==expectedAnswerPixels){
                theAnswer=optionName;
                break;
              }else{
                if(accuracyPercentage<0.5){
                  if(accuracyPercentage<nearestAccuracy){
                    nearestAccuracy=accuracyPercentage;
                    theAnswer=optionName;
                  }
                }
                  answers.put(optionName,accuracyPercentage);

              }
            
          }
        

              
          }
        }
      }

  return theAnswer;
 }

public String findAnswerColumn(HashMap<String,Integer> problemInPixels,HashMap<String,RavensFigure> problemFigures, HashMap<String, Integer[][]> problemPixelMap){
String theAnswer="";
String numRegex="^\\d+$";
    Set<String> pixelMapOptions=problemPixelMap.keySet();

        Integer[][] columnWiseCommon=findCommonObject(problemPixelMap.get("G"),problemPixelMap.get("H"));
        Integer[][] rowWiseCommon=findCommonObject(problemPixelMap.get("C"),problemPixelMap.get("F"));

        Integer[][] merged=mergeTwoOptions(columnWiseCommon,rowWiseCommon);

        int expectedPixelCount=0;
        for(int i=0;i<184;i++){
          for(int j=0;j<184;j++){
            if(merged[j][i]!=0)
                expectedPixelCount=expectedPixelCount+1;
          }
          
        }

        Set<String> optionNames=problemInPixels.keySet();
        double firstColumn;
        double nearestHighAccuracy=0.5;
        double nearestLowAccuracy=0.0;
        HashMap<String,Double> pixelCompare=new HashMap<String,Double>();
        HashMap<String,Double> expectCompare=new HashMap<String,Double>();
        
        for(String optionName: optionNames){
          if(optionName.matches(numRegex)){
          int optionPixels=problemInPixels.get(optionName);

          if(expectedPixelCount==optionPixels){
            theAnswer=optionName;
            break;
          }else{
            Integer[][] optionCompare=findCommonObject(merged,problemPixelMap.get(optionName));
            double accuracyPercentage=compareObjects(merged,optionCompare);
            double accuracyPercentage2=(Math.abs(expectedPixelCount-optionPixels))/(double)((expectedPixelCount>optionPixels)?optionPixels:expectedPixelCount);
            // System.out.println("Option "+optionName+" PixelCount "+optionPixels+" Accuracy1 "+accuracyPercentage+" Accuracy2 "+accuracyPercentage2);     
            if(accuracyPercentage==nearestLowAccuracy){
              
                nearestLowAccuracy=accuracyPercentage;
                pixelCompare.put(optionName,accuracyPercentage);
                expectCompare.put(optionName,accuracyPercentage2);
            }else if(accuracyPercentage>nearestLowAccuracy){
              
              nearestLowAccuracy=accuracyPercentage;
              pixelCompare.put(optionName,accuracyPercentage);
              expectCompare.clear();
              expectCompare.put(optionName,accuracyPercentage2);
            }

          }  
          }
          

        }
        if(theAnswer.equalsIgnoreCase("")){


          Set<String> options=expectCompare.keySet();
            for(String option:options){
              // System.out.println("Option coming is "+option+"Value"+expectCompare.get(option));
              if(expectCompare.get(option)<nearestHighAccuracy){
                nearestHighAccuracy=expectCompare.get(option);
                theAnswer=option;
              }
            }
        }

      return theAnswer;

}

public double compareObjects(Integer[][] a,Integer[][] b){
  double accuracyPercentage=0.0;
  int pixelsEquals=0;
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      if(a[i][j]==b[i][j])
        pixelsEquals=pixelsEquals+1;
    }
  }
  accuracyPercentage=pixelsEquals/(double)(184*184);
  return accuracyPercentage;
}

public Integer[][] mergeTwoOptions(Integer[][] a,Integer[][] b){
  Integer[][] colCommonObject=new Integer[184][184];
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      colCommonObject[j][i]=a[j][i]|b[j][i];
    }
  }
  return colCommonObject;
}

public Integer[][] diffTwoOptions(Integer[][] a,Integer[][] b){
  Integer[][] colCommonObject=new Integer[184][184];
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      colCommonObject[j][i]=a[j][i]-b[j][i];
    }
  }
  return colCommonObject;
}

public Integer[][] xorTwoOptions(Integer[][] a,Integer[][] b){
  Integer[][] colCommonObject=new Integer[184][184];
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      colCommonObject[j][i]=a[j][i] ^ b[j][i];
    }
  }
  return colCommonObject;
}

public Integer[][] interesctTwoOptions(Integer[][] a,Integer[][] b){
  Integer[][] colCommonObject=new Integer[184][184];
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      colCommonObject[j][i]=a[j][i]&b[j][i];
    }
  }
  return colCommonObject;
}

public Integer[][] findCommonObject(Integer[][] a,Integer[][] b){
  Integer[][] commonObject=new Integer[184][184];
  for(int i=0;i<184;i++){
    for(int j=0;j<184;j++){
      commonObject[j][i]=a[j][i]&b[j][i];
    }
  }
  return commonObject;
}

public HashMap<String,Integer[][]> findPixelMap(HashMap<String,RavensFigure> problemFigures){
    HashMap<String,Integer[][]> pixelMap=new HashMap<String,Integer[][]>();

    Set<String> optionImageNames=problemFigures.keySet();
    try{
      for(String option: optionImageNames){
        RavensFigure thisFigure=problemFigures.get(option);
        Integer[][] thisOptionPixelMap=new Integer[184][184];
        BufferedImage thisOptionImage=ImageIO.read(new File(thisFigure.getVisual()));
        int pixelCount=0;
        for(int i=0;i<thisOptionImage.getHeight();i++){
          for(int j=0;j<thisOptionImage.getWidth();j++){
              if(thisOptionImage.getRGB(i,j)== 0xFFFFFFFF){
                thisOptionPixelMap[i][j]=0;
              }else{
                thisOptionPixelMap[i][j]=1;
              }
                
          }
        }
        pixelMap.put(option,thisOptionPixelMap);
      }
    }catch(Exception e){
                    e.printStackTrace();
    }

    return pixelMap;

}

public HashMap<String,String> findHorizontalDiff(HashMap<String,String> t1,HashMap<String,String> t2){
        HashMap<String,String> horizontalDiff=new HashMap<>();
        Set<String> valuesOft1=t1.keySet();
        int noOfKeys=valuesOft1.size();
        return horizontalDiff;
    }

    public HashMap<String,String> findTransformation(RavensFigure A, RavensFigure B){
        
        
        HashMap<String,RavensObject> valuesOfA=A.getObjects();
        HashMap<String,RavensObject> valuesOfB=B.getObjects();
        HashMap<String,String> transformation=new HashMap<>();
        Set<String> typesOfValuesA=valuesOfA.keySet();
        Set<String> typesOfValuesB=valuesOfB.keySet();
        int noOfKeys=typesOfValuesA.size();
        System.out.println(" 444sdf"+noOfKeys);

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
                        if(i>0){
                            
                            transformation.put(a+i,identifyTransformation(a,attributesofA.get(a),attributesofB.get(a)));
                        }
                            
                        else{
                            
                            transformation.put(a,identifyTransformation(a,attributesofA.get(a),attributesofB.get(a)));    
                        }
                            
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
 public String compareAnswer(int a,HashMap<String,Integer> problemInPixels){
   String answer="";
   HashMap<String,Double> probans=new HashMap<String,Double>();
   String numRegex="^\\d+$";
   Set<String> problem=problemInPixels.keySet();
   for(String option:problem){
     if(option.matches(numRegex)){
      //  System.out.println(a+"=="+problemInPixels.get(option));
        if(problemInPixels.get(option)==a)
          answer=option;
      double r=Math.abs(problemInPixels.get(option)-a)*100/((problemInPixels.get(option)>a)?a:problemInPixels.get(option));
      System.out.println(r);
        if(r>98)
            probans.put(option,r);
     }
   }
  //  System.out.println(probans.size());
   if(probans.size()>1)
      answer="checkagains";



      return answer;

 }

}
