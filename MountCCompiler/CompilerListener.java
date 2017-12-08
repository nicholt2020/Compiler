-/*
-   This listener generates Pep/9 assembly for a (mini) MountC program.
-   Of the four i/o functions of MountC, it only predefines putint.
-   K. Weber
-   weberk@mountunion.edu
-   10-nov-2017
-*/
-
-import java.util.HashMap;
-
-public class CompilerListener extends MountCBaseListener {
-
-  /*  Here is a primitive symbol table for function definitions.
-   *  It stores the number of arguments in the formal argument list
-      for a function, using the function's name as the key.
-  */
-  private HashMap<String, Integer> symtab =  new HashMap<>();
-   
-  /* Here is a primitive symbol table for parameters.
-   * It stores the name of var EX: x for skyFun would be stored as "skyFun_x".
-   * It also stores the position of the parameter on the stack frame from the stack pointer.
-  */
-  private HashMap<String, Integer> params =  new HashMap<>();
-
-   
-  // 
-  private int ifIDGenerator = 1;
-   
-  //
-  private int ifID = 1;
-   
-  /* functionName is used to record the name of the current function whenever you enter a
-   * function call so you can acturately retrieve variables from the params HashMap.
-  */ 
-  private String functionName = null;
-   
-  /* Offset is used to calculate the distance to parameters when other changes are made to the 
-   * stack pointer after variable declaration.
-  */
-  private int offset = 0;
-
-  /* Push is used whenever you need to "make room" on the stack for some operation.
-   * Push accepts a Boolean parameter as you may not always want to store the accumulator when 
-     you "make room".
-   * Push also adds to the offset to keep track of variable location.
-  */ 
-  private void push(Boolean dontSTWA){
-    System.out.println("\tSUBSP\t2,i");
-    if(!dontSTWA){
-      System.out.println("\tSTWA\t0,s");
-    }
-    offset+=2;
-  }
-
-  /* Pop is used whenever you need to "clean-up" the stack for some operation.
-   * Pop accepts an integer parameter that is used to pass through how many bytes to add to the 
-     stack pointer.
-   * Pop also subtracts from the offset to keep track of variable location.
-  */
-  private void pop(int numBytesToPop){
-    System.out.println("\tADDSP\t" + numBytesToPop + ",i");
-    offset-=numBytesToPop;
-  }
-
-  /* enterProgram is used to setup the initials methods of any legal MountC Program.
-   * Methods defined here are: putint(), getint(), getchar() and putchar().
-  */  -/*
-   This listener generates Pep/9 assembly for a (mini) MountC program.
-   Of the four i/o functions of MountC, it only predefines putint.
-   K. Weber
-   weberk@mountunion.edu
-   10-nov-2017
-*/
-
-import java.util.HashMap;
-
-public class CompilerListener extends MountCBaseListener {
-
-  /*  Here is a primitive symbol table for function definitions.
-   *  It stores the number of arguments in the formal argument list
-      for a function, using the function's name as the key.
-  */
-  private HashMap<String, Integer> symtab =  new HashMap<>();
-   
-  /* Here is a primitive symbol table for parameters.
-   * It stores the name of var EX: x for skyFun would be stored as "skyFun_x".
-   * It also stores the position of the parameter on the stack frame from the stack pointer.
-  */
-  private HashMap<String, Integer> params =  new HashMap<>();
-
-   
-  // 
-  private int ifIDGenerator = 1;
-   
-  //
-  private int ifID = 1;
-   
-  /* functionName is used to record the name of the current function whenever you enter a
-   * function call so you can acturately retrieve variables from the params HashMap.
-  */ 
-  private String functionName = null;
-   
-  /* Offset is used to calculate the distance to parameters when other changes are made to the 
-   * stack pointer after variable declaration.
-  */
-  private int offset = 0;
-
-  /* Push is used whenever you need to "make room" on the stack for some operation.
-   * Push accepts a Boolean parameter as you may not always want to store the accumulator when 
-     you "make room".
-   * Push also adds to the offset to keep track of variable location.
-  */ 
-  private void push(Boolean dontSTWA){
-    System.out.println("\tSUBSP\t2,i");
-    if(!dontSTWA){
-      System.out.println("\tSTWA\t0,s");
-    }
-    offset+=2;
-  }
-
-  /* Pop is used whenever you need to "clean-up" the stack for some operation.
-   * Pop accepts an integer parameter that is used to pass through how many bytes to add to the 
-     stack pointer.
-   * Pop also subtracts from the offset to keep track of variable location.
-  */
-  private void pop(int numBytesToPop){
-    System.out.println("\tADDSP\t" + numBytesToPop + ",i");
-    offset-=numBytesToPop;
-  }
-
-  /* enterProgram is used to setup the initials methods of any legal MountC Program.
-   * Methods defined here are: putint(), getint(), getchar() and putchar().
-  */

  @Override
  public void exitEquExp(MountCParser.EquExpContext ctx) {
    //System.out.println(ctx.getParent().getChild(0).toString());
      if(params.containsKey(functionName+"_"+ctx.getParent().getChild(0).toString())){
          int location = params.get(functionName+"_"+ctx.getParent().getChild(0).toString()) + offset;
          System.out.println("\tSTWA\t" + location + ",s");
      }
  }


  @Override
  public void exitFun_def(MountCParser.Fun_defContext ctx) {
    Integer numParams = symtab.get(functionName);
    System.out.println("\tSTWA\t"+ (offset + (2*numParams + 2)) + ",s");
    System.out.println("\tRET");
  }

  @Override
  public void enterNumTerm(MountCParser.NumTermContext ctx) {
        System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
  }

  @Override
  public void enterIdTerm(MountCParser.IdTermContext ctx) {
    if(params.containsKey(functionName + "_" + ctx.getChild(0).toString())){
    // System.out.println(functionName + "_" + ctx.getChild(0).toString());
        int location = params.get(functionName+ "_" +ctx.getChild(0).toString()) + offset;
        System.out.println("\tLDWA\t" + location + ",s");
        //System.out.println("\tSTWA\t" + location + ",s");
    }
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

      //if(ctx.getParent().getParent().getParent().getClass().equals(MountCParser.ArgListExprContext.class)){
      //  System.out.println("\tLDWA\t-4,s");
      //} else {
        System.out.println("\tLDWA\t-2,s");
      //}
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
    //System.out.println("ddd");
  }

  @Override
  public void enterEmptyArglist(MountCParser.EmptyArglistContext ctx) {
    push(false);
  //  System.out.println("fff");

  }

  //  If Statments ------------
  @Override
  public void enterIfExpr(MountCParser.IfExprContext ctx) {
  	ifIDGenerator=((ifIDGenerator/ifID)+1)*ifID;
  }

  @Override
  public void exitIfExpr(MountCParser.IfExprContext ctx) {
  	System.out.println("__end"+ifIDGenerator+":\tNOP0");
  	ifIDGenerator= ((ifIDGenerator/ifID)-1)*ifID;
  	if (ifIDGenerator/ifID == 1){
  		ifID = ifID*1000;
  	}
  }

  @Override
  public void enterThenPart(MountCParser.ThenPartContext ctx) {
      System.out.println("\tCPWA\t0,i");
      System.out.println("\tBREQ\t__else"+ifIDGenerator);

  }

  @Override
  public void enterElsePart(MountCParser.ElsePartContext ctx) {
  	System.out.println("\tBR\t__end"+ifIDGenerator);
  	System.out.println("__else"+ifIDGenerator+":\tNOP0");
  }


}
