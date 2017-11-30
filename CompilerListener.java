/*
   This listener generates Pep/9 assembly for a (mini) MountC program.
   Of the four i/o functions of MountC, it only predefines putint.
   K. Weber
   weberk@mountunion.edu
   10-nov-2017
*/

import java.util.HashMap;

public class CompilerListener extends MountCBaseListener {

  //  Here is a primitive symbol table for function definitions.
  //  It stores the number of arguments in the formal argument list
  //  for a function, using the function's name as the key.
  private HashMap<String, Integer> symtab =  new HashMap<>();
  private HashMap<String, Integer> params =  new HashMap<>();

  private int ifIDGenerator = 1;
  private int ifID = 1;
  private String functionName = null;
  private int offset = 0;

  private void push(Boolean dontSTWA){
    System.out.println("\tSUBSP\t2,i");
    if(!dontSTWA){
      System.out.println("\tSTWA\t0,s");
    }
    offset+=2;
  }

  private void pop(int numBytesToPop){
    System.out.println("\tADDSP\t" + numBytesToPop + ",i");
    offset-=numBytesToPop;
  }

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
  @Override
  public void exitProgram(MountCParser.ProgramContext ctx) {
    System.out.println("\t.END");
  }

  @Override
  public void enterFun_def(MountCParser.Fun_defContext ctx) {
    String id = ctx.ID().toString(); // Get the ID We're working with ex: MAIN

    int numParams;
    if(ctx.getChild(2).getChildCount() != 0){ // If the Num Params is not 0 then calculate the Num Paramas
      numParams = (ctx.getChild(2).getChildCount() / 2) + 1;
    } else {
      numParams = 0;
    }

    if(!symtab.containsKey(id)){ // If function not already inside symtab add it in.
      symtab.put(id, numParams);
    }

    int numBytes = 2*(symtab.get(id) + 1) - 2;

    for(int i = 0; i < ctx.getChild(2).getChildCount(); i += 2){ // Loop through the number of params and Declare them all in PEP 9
      System.out.println(";"+id + "_" + ctx.getChild(2).getChild(i).toString() +" "+numBytes);
      params.put(id + "_" + ctx.getChild(2).getChild(i).toString(), numBytes);
      numBytes -= 2;
      //System.out.println(id + "_" + ctx.getChild(2).getChild(i).toString() + ", " + i);
    }

    System.out.print("\n");
    System.out.println(id + ":\tNOP0");

    functionName = id;
  }

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
    System.out.println("\tSTWA\t2,s");
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
        int location = params.get(functionName+ "_" +ctx.getChild(0).toString())+ offset;
        System.out.println("\tLDWA\t" + location + ",s");
        System.out.println("\tSTWA\t" + location + ",s");
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
    //if (numParams != ctx.getChild(0).getChildCount()) {
    //   System.err.println("function " + id + " called with wrong number of arguments");
    //   System.exit(2);
    //}
    push(true);
  }

  @Override
  public void exitFunCall(MountCParser.FunCallContext ctx) {
      String id = ctx.getParent().getChild(0).toString();
      System.out.println("\tCALL\t" + id);
      int numBytesToPop = 2*(symtab.get(id) + 1);
      pop(numBytesToPop);

      if(ctx.getParent().getParent().getParent().getClass().equals(MountCParser.ArgListExprContext.class)){
        System.out.println("\tLDWA\t-4,s");
      } else {
        System.out.println("\tLDWA\t-2,s");
      }
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
