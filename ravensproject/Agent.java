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
        try{
          if(problem.hasVisual()){
            HashMap<String, RavensFigure> problemFigures=problem.getFigures();
            HashMap<String,Integer> problemInPixels=findthePixels(problemFigures);
            HashMap<String,String> problemTransformation=findtheTransformation(problemInPixels);
            //answer=Integer.parseInt(findtheAnswer(problemInPixels,problemTransformation));
            // Set<String> transformations=problemTransformation.keySet();
            // for(String s: transformations){
            //   System.out.println(s+"  "+problemTransformation.get(s));
            // }
           System.out.println("Visual is avaialable");

          }else{
            System.out.println("No Visual Avaialable");
          }

        }catch(Exception e){
          e.printStackTrace();
        }
        return answer;
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
  //  String firstRowComparison=
  String abTransition=findTransistion(problemInPixels.get("A"),problemInPixels.get("B"));
  String bcTransition=findTransistion(problemInPixels.get("B"),problemInPixels.get("C"));
  String deTransition=findTransistion(problemInPixels.get("D"),problemInPixels.get("E"));
  String efTransition=findTransistion(problemInPixels.get("E"),problemInPixels.get("F"));
  String ghTransition=findTransistion(problemInPixels.get("G"),problemInPixels.get("H"));

  // System.out.println(findTransistion(problemInPixels.get("A"),problemInPixels.get("B")));
  //  if(firstRowComparison.equalsIgnoreCase("no-change"))
  if((abTransition==bcTransition)&&(deTransition==efTransition))
      transformation.put("pixelChange","no-change");
      transformation.put("transitionChange","no-change");
 else{
   if((abTransition==deTransition)&&(bcTransition==efTransition)){
     transformation.put("pixelChange","column");
     transformation.put("transitionChange","column");
   }else{
     tranformation.put("transitionChange","cyclic");
     transformation.put("pixelChange","cyclic");
   }

 }


  System.out.println(findTransistion(problemInPixels.get("B"),problemInPixels.get("C")));
  System.out.println(findTransistion(problemInPixels.get("D") ,problemInPixels.get("E")));
  System.out.println(findTransistion(problemInPixels.get("E"),problemInPixels.get("F")));
  System.out.println(findTransistion(problemInPixels.get("G"),problemInPixels.get("H")));
    //else
    //  transformation.put("pixelChange","")
    // transformation.put("1",);
    // transformation.put("2",);
    // transformation.put("3",findTransistion(problemInPixels.get("D"),problemInPixels.get("E")));
    // transformation.put("4",findTransistion(problemInPixels.get("E"),problemInPixels.get("F")));
    //    //String firstColumnComparison=findTransistion(problemInPixels.get("A"),problemInPixels.get("D"),problemInPixels.get("G"));

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

 public String findtheAnswer(HashMap<String,Integer> problemInPixels,HashMap<String,String> tranformation){
   String theAnswer="";
   if(tranformation.get("pixelChange").equalsIgnoreCase("no-change")){
              theAnswer=compareAnswer(problemInPixels.get("G"),problemInPixels);
   }
   return theAnswer;
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
      double r=Math.abs(problemInPixels.get(option)-a)*100/((problemInPixels.get(option)>a)?problemInPixels.get(option):a);
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
