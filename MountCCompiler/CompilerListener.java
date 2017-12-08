/*
   This listener generates Pep/9 assembly for a (mini) MountC program.
   Of the four i/o functions of MountC.
   Authors:
   Name: Skyler Knecht; Email: knechtsr2019@mountunion.edu
   Name: Gaston C. Marian; Email: marianga@mountunion.edu
   
   Credits Due:
   Name: Dr. Weber Kenneth; Email: weberk@mountunion.edu
  
   Last Updated: Dec 8, 2017 9:47AM
*/

import java.util.HashMap;

public class CompilerListener extends MountCBaseListener {

  //  Here is a primitive symbol table for function definitions.
  //  It stores the number of arguments in the formal argument list
  //  for a function, using the function's name as the key.
  private HashMap<String, Integer> symtab =  new HashMap<>();
   
  
 /* Here is a primitive symbol table for parameters.
  * It stores the name of var EX: x for skyFun would be stored as "skyFun_x".
  * It also stores the position of the parameter on the stack frame from the stack pointer.
  */
  private HashMap<String, Integer> params =  new HashMap<>();

 /* ifIDGenerator & ifID are int variables that are used to keep track of if statements as you go in and out of various levels.  
  * they are currently multiplied together, with the scale being multiplied by 1000 every time to move
  * to another level of nesting. Upon thinking about it, it would likely be clearer to write IDs as
  * ifIDGenerator&ifID rather than ifIDGenerator*ifID, but it's already done.
  */
  private int ifIDGenerator = 1;
  private int ifID = 1;
   
 /* functionName is used to record the name of the current function whenever you enter a
  * function call so you can acturately retrieve variables from the params HashMap.
  */ 
  private String functionName = null;
   
 /* Offset is used to calculate the distance to parameters when other changes are made to the 
  * stack pointer after variable declaration.
  */
  private int offset = 0;

 /* Push is used whenever you need to "make room" on the stack for some operation.
  * Push accepts a Boolean parameter as you may not always want to store the accumulator when 
    you "make room".
  * Push also adds to the offset to keep track of variable location.
  */ 
  private void push(Boolean dontSTWA){
    System.out.println("\tSUBSP\t2,i");
    if(!dontSTWA){
      System.out.println("\tSTWA\t0,s");
    }
    offset+=2;
  }

 /* Pop is used whenever you need to "clean-up" the stack for some operation.
  * Pop accepts an integer parameter that is used to pass through how many bytes to add to the 
    stack pointer.
  * Pop also subtracts from the offset to keep track of variable location.
  */
  private void pop(int numBytesToPop){
    System.out.println("\tADDSP\t" + numBytesToPop + ",i");
    offset-=numBytesToPop;
  }

 /* enterProgram is used to setup the initials methods of any legal MountC Program.
  * Methods defined here are: putint(), getint(), getchar() and putchar().
  */
  @Override
  public void enterProgram(MountCParser.ProgramContext ctx) {
    System.out.print("\n");
    System.out.println(";Start Of Main Method");
    System.out.println("\tSUBSP\t2,i");
    System.out.println("\tCALL\tmain");
    System.out.println("\tADDSP\t2,i");
    System.out.println("\tSTOP");
    System.out.print("\n");
    System.out.println(";putint will print out an integer value.");
    System.out.println("putint:\tDECO\t2,s"); // putInt()
    System.out.println("\tLDWA\t2,s");
    System.out.println("\tSTWA\t4,s");
    System.out.println("\tRET");
    symtab.put("putint", 1);
    System.out.print("\n");
    System.out.println(";getint will retrieve input.");
    System.out.println("getint:\tDECI\t2,s"); // getint()
    System.out.println("\tRET");
    symtab.put("getint", 0);
    System.out.print("\n");
    System.out.println(";getchar will retrieve input and store the ASCII equivelant.");
    System.out.println("getchar:\tLDWA\t0,i"); // getChar()
    System.out.println("\tLDBA\tcharIn,d");
    System.out.println("\tSTWA\t2,s");
    System.out.println("\tRET");
    symtab.put("getchar", 0);
    System.out.print("\n");
    System.out.println(";putchar will print out an ASCII equivelant.");
    System.out.println("putchar:\tLDWA\t2,s"); // putChar()
    System.out.println("\tSTBA\tcharOut,d");
    System.out.println("\tSTWA\t4,s");
    System.out.println("\tRET");
    symtab.put("putchar", 1);

  }
   
 /* exitProgram will conclude the compile of a legal MountC program with a .END as required for
  * Pep/9 asembly.
  */
  @Override
  public void exitProgram(MountCParser.ProgramContext ctx) {
    System.out.println("\t.END");
  }
   
 /* enterFun_def (EFD) is called whenever a new MountC function is being created.
  * EFD will collect the ID or NAME of the function trying to be created EX: Main.
  * Then EFD will calculate the number of params of the function.
  * EFD will continue to check to see if the function is in the symtab.
  * If the function is not in the symtab then EFD will add the current function along with # of params.
  * EFD will continue to use the number of parameters to loop through and declare all needed params in PEP/9 in 
    the correction position on the stack.
  * EFD the continues by starting the function definition with EX: Main NOP0 and setting the function id to the global
     string functionName. 
  */
  @Override
  public void enterFun_def(MountCParser.Fun_defContext ctx) {
    offset = 0;
    String id = ctx.ID().toString(); // Get the ID We're working with ex: MAIN

    int numParams;
    if(ctx.getChild(2).getChildCount() != 0){ // If the Num Params is not 0 then calculate the Num Paramas
      numParams = (ctx.getChild(2).getChildCount() / 2) + 1;
    } else {
      numParams = 0;
    }

    if(!symtab.containsKey(id)){ // If function not already inside symtab add it in.
      symtab.put(id, numParams);
    } else {
      System.err.println("function " + id + " already defined at this point");
      System.exit(5);
    }

    int numBytes = 2*(symtab.get(id) + 1) - 2;

    for(int i = 0; i < ctx.getChild(2).getChildCount(); i += 2){ // Loop through the number of params and Declare them all in PEP 9
      params.put(id + "_" + ctx.getChild(2).getChild(i).toString(), numBytes);
      numBytes -= 2;
    }

    System.out.print("\n");
    System.out.println(id + ":\tNOP0");

    functionName = id;
  }
    
 /* exitFun_def will store the number of params of the current function and use that to store the current 
    accumulator into the RV of the function. Then the method will continue by printing out RET as required for 
    PEP9 asembly.
  */ 
  @Override
  public void exitFun_def(MountCParser.Fun_defContext ctx) {
    Integer numParams = symtab.get(functionName);
    System.out.println("\tSTWA\t"+ (offset + (2*numParams + 2)) + ",s");
    System.out.println("\tRET");
  }

 /* enterNumTerm will load what numerical value is being entered into the accumulator for use in 
    expressions, storage ect.. EX: y = 7; , reutrn 7 + 17 + 27;
  */
  @Override
  public void enterNumTerm(MountCParser.NumTermContext ctx) {
        System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
  }
   
   
 /* enterIdTerm will load the value in the location of whatever ID the parser just entered.
    EX: putint(y) - the program will load whatever is in Y into the accumulator for later use in putint()
  */
  @Override
  public void enterIdTerm(MountCParser.IdTermContext ctx) {
    if(params.containsKey(functionName + "_" + ctx.getChild(0).toString())){
       int location = params.get(functionName+ "_" +ctx.getChild(0).toString()) + offset;
       System.out.println("\tLDWA\t" + location + ",s");
    } else {
       System.err.println("Parameter: " + ctx.getParent().getChild(0).toString() + " doesen't exist.");
       System.exit(4);
    }
  }
   
 /* exitEquExp will store expressions into any defined parameteres.
  * A example is something like y = 7 + 17; if y is defined the location of y will be overwritten.
  * There is not test needed to see if the ID is defined EX: 'y' as the error will already be thrown when
  * enterIDTerm is called.
  */
  @Override
  public void exitEquExp(MountCParser.EquExpContext ctx) {
       int location = params.get(functionName+"_"+ctx.getParent().getChild(0).toString()) + offset;
       System.out.println("\tSTWA\t" + location + ",s");
  }


  @Override
  public void enterFunCall(MountCParser.FunCallContext ctx) {
    String id = ctx.getParent().getChild(0).toString();
    Integer numParams = symtab.get(id);
    if (numParams == null) {
       System.err.println("function " + id + " not defined at this point");
       System.exit(1);
    }
    if (numParams != ctx.getChild(0).getChildCount()) {
       System.err.println("function " + id + " called with wrong number of arguments");
       System.exit(2);
    }
    push(true);
  }

  @Override
  public void exitFunCall(MountCParser.FunCallContext ctx) {
      String id = ctx.getParent().getChild(0).toString();
      System.out.println("\tCALL\t" + id);
      int numBytesToPop = 2*(symtab.get(id) + 1);
      pop(numBytesToPop);
      System.out.println("\tLDWA\t-2,s");
  }

  @Override public void exitExprList(MountCParser.ExprListContext ctx) {
    if(ctx.getParent().getChild(0).getChild(0).toString().equals("-")){
      System.out.println("\tNEGA");
    }

    // When you exit an exprList add the current subtotal to the Accumulator and cleanup the subtotal with ADDSP.
    if(ctx.getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class) || ctx.getParent().getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class)){
      System.out.println("\tADDA\t0,s");
      pop(2);
    }
  }


  @Override
  public void enterExpr_tail(MountCParser.Expr_tailContext ctx) {
      if(!ctx.getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class)){
          push(false);
      } else {
          if(ctx.getParent().getParent().getChild(0).getChild(0).toString().equals("-")){
             System.out.println("\tNEGA");
          }
            System.out.println("\tADDA\t0,s");
            System.out.println("\tSTWA\t0,s");
      }
   }


  @Override
  public void exitExpr_tail(MountCParser.Expr_tailContext ctx) {
      if(ctx.getChild(1).getChildCount() == 1){
        if(ctx.getChild(0).getChild(0).toString().equals("-")){
          System.out.println("\tNEGA\t");
        }
          System.out.println("\tADDA\t0,s");
          System.out.println("\tSTWA\t0,s");
          pop(2);
      }
  }

  @Override
  public void enterActualArg(MountCParser.ActualArgContext ctx){
    push(false);
  }

  @Override
  public void enterEmptyArglist(MountCParser.EmptyArglistContext ctx) {
    push(false); 
  }

  //  If Statments ------------
 
 /* Every time an if expression is entered on the parse tree, a new ifIDGenerator
  * is generated by iterating the ifIDGenerator variable to indicate the depth the
  * expression is nested at, and multiplied by ifID to indicate the occurence of
  * if expression in the program as a whole. This allows for the generation of
  * unique else and end tags to branch our Pep/9 program to
  */
  @Override
  public void enterIfExpr(MountCParser.IfExprContext ctx) {
  	ifIDGenerator=((ifIDGenerator/ifID)+1)*ifID;
  }

   
 /* On exiting an ifExpression on the parse tree, this method does a lot of the 
  * clean up and maintenence. It creates the end tag that the if can branch to
  * with the unique ID and a NOP0, then it subtracts one from the ifIDGenerator
  * to indicate getting out of a level of if statements, should they be nested. 
  * Next, a check is done to see if ifIDGenerator is back to 1, in which case
  * it has left the top level if statement, so it iterates the ifID by 1000
  * so the next if statement can be distinct.
  */
  @Override
  public void exitIfExpr(MountCParser.IfExprContext ctx) {
  	System.out.println("__end"+ifIDGenerator+":\tNOP0");
  	ifIDGenerator= ((ifIDGenerator/ifID)-1)*ifID;
  	if (ifIDGenerator/ifID == 1){
  		ifID = ifID*1000;
  	}
  }

   
 /* Because whatever precedes this on the tree, either a num term or expression,
  * will always end its process by writing its value to the accumulator, this
  * method can just compare the word in the accumulator to 0 directly. It then
  * breaks to the else tag if it is in fact equal to 0.
  */
  @Override
  public void enterThenPart(MountCParser.ThenPartContext ctx) {
      System.out.println("\tCPWA\t0,i");
      System.out.println("\tBREQ\t__else"+ifIDGenerator);

  }

 /* On entering the elsePart, the previous expression will already have been set
  * up for the program to run through if the value is not equal to 0. This creates
  * a branch to the end, after that not equal branch is followed. This also sets
  * up the unique else tag that the code can branch to if the accumulator equals 0.
  */  
  @Override
  public void enterElsePart(MountCParser.ElsePartContext ctx) {
  	System.out.println("\tBR\t__end"+ifIDGenerator);
  	System.out.println("__else"+ifIDGenerator+":\tNOP0");
  }


}
