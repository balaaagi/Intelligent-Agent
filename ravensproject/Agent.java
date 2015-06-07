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
        HashMap<String, RavensFigure> figures=new HashMap<>();
        figures=problem.getFigures();
        
        Set<String> figuresNames=figures.keySet();

        
        RavensFigure optionA=figures.get("A");
        RavensFigure optinoB=figures.get("B");
        HashMap<String,String> transformation1=findTransformation(figures.get("A"),figures.get("B"));
        //HashMap<String,String> transformation2=findTransformation(figures.get("A"),figures.get("C"));
        HashMap<String,String> probableSolution=applyTransformation(figures.get("C"),transformation1);
        String numRegex="^\\d+$";
        int ansRating=0;
        String ansOption="-1";
        for(String figureName: figuresNames){
            if(figureName.matches(numRegex)){
                RavensFigure option =figures.get(figureName);
                int thisOptionRating=compareProbables(probableSolution,option);
                if(thisOptionRating>ansRating){
                    ansRating=thisOptionRating;
                    ansOption=figureName;
                }


            }
        

        }
        if(ansRating>9){
            return Integer.parseInt(ansOption);
        }else
            return -1;

        
    }

    public HashMap<String,String> findTransformation(RavensFigure A, RavensFigure B){
        System.out.println("Inside transformation-------");
        
        HashMap<String,RavensObject> valuesOfA=A.getObjects();
        HashMap<String,RavensObject> valuesOfB=B.getObjects();
        HashMap<String,String> transformation=new HashMap<>();
        Set<String> typesOfValuesA=valuesOfA.keySet();
        Set<String> typesOfValuesB=valuesOfB.keySet();
        int noOfKeys=typesOfValuesA.size();
        System.out.println(noOfKeys+"&&&&");

        for(int i=0;i<noOfKeys;i++){
            System.out.println(typesOfValuesA.toArray()[i]);
            HashMap<String,String> attributesofA=valuesOfA.get(typesOfValuesA.toArray()[i]).getAttributes();
            HashMap<String,String> attributesofB=valuesOfB.get(typesOfValuesB.toArray()[i]).getAttributes();

            Set<String> attributesofANames=attributesofA.keySet();
            for(String a: attributesofANames){
                System.out.println(a+"****");
                transformation.put(a,identifyTransformation(a,attributesofA.get(a),attributesofB.get(a)));
                        
            }
            
        }
        
        
        System.out.println("Ends transformation-------");
        System.out.println(transformation);
        return transformation;
    }

    public String identifyTransformation(String attributeName,String attributeValue1,String attributeValue2){
        if(attributeValue1.equalsIgnoreCase(attributeValue2))
            return "nochange";
        else
            return "";
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
                if(transformation.get(a).equalsIgnoreCase("nochange"))
                    probableSolution.put(a,attributesofC.get(a));
            }
        }
        return probableSolution;
    }

    public int compareProbables(HashMap<String,String> probableSolution,RavensFigure option){
        int tempRating=0,noOfAttributes=0;;
        HashMap<String,RavensObject> valueOfOption=option.getObjects();
        Set<String> typesOfValuesOption=valueOfOption.keySet();
        int noOfKeys=typesOfValuesOption.size();
        for(int i=0;i<noOfKeys;i++){
            HashMap<String,String> attributesofOption=valueOfOption.get(typesOfValuesOption.toArray()[i]).getAttributes();
            Set<String> attributesofOptionNames=attributesofOption.keySet();
            noOfAttributes=attributesofOptionNames.size();
            for(String a: attributesofOptionNames){
                if(probableSolution.get(a).equalsIgnoreCase(attributesofOption.get(a)))
                    tempRating++;

            }
        }
        return (tempRating/noOfAttributes)*10;

    }

    public String testFunction(String message){
        return message;
    }
}
