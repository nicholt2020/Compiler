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
  private boolean minus = false;

  @Override
  public void enterProgram(MountCParser.ProgramContext ctx) {
    System.out.println("s:\t.EQUATE 0");
    System.out.println("\n\tSUBSP\t4,i");
    System.out.println("\tCALL\tmain");
    System.out.println("\tADDSP\t4,i");
    System.out.println("\tSTOP");
    System.out.println("putint:\tDECO\t2,s");
    System.out.println("\tLDWA\t2,s");
    System.out.println("\tSTWA\t4,s");  //  return the same value we printed out.
    System.out.println("\tRET");
    symtab.put("putint", 1);
    System.out.println("getint:\tDECI\t2,s");
    System.out.println("\tRET");
    symtab.put("getint", 0);
  }
  @Override
  public void exitProgram(MountCParser.ProgramContext ctx) {
    System.out.println("\t.END");
  }

  @Override
  public void enterFun_def(MountCParser.Fun_defContext ctx) {
    String id = ctx.ID().toString();
    System.out.println(id + ":\tNOP0");
  }

  @Override
  public void exitFun_def(MountCParser.Fun_defContext ctx) {
    System.out.println("\tSTWA\t2,s");
    System.out.println("\tRET");
  }

  @Override
  public void enterNumTerm(MountCParser.NumTermContext ctx) {
        //if(ctx.getParent().getParent().getChild(0).getChild(0).toString().equals("-")){
        //   System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
        //   System.out.println("\tNEGA\t");
        //} else {
        //  System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
        //}

        String op = ctx.getParent().getParent().getChild(0).getChild(0).toString();
        switch(op){
          case "-": //System.out.println("\tSUBSP\t2,i");
                    //System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
                    //System.out.println("\tNEGA\t");
                    //System.out.println("\tSTWA\t0,s");
                    //System.out.println("\tLDWA\ts,x");
                    //System.out.println("\tADDA\t0,s");
                    //System.out.println("\tSTWA\ts,x");
                    //System.out.println("\tADDSP\t2,i");
                    System.out.println("\tLDWA\ts,x");
                    System.out.println("\tSUBA\t" + ctx.NUM() + ",i");
                    System.out.println("\tSTWA\ts,x");
                    break;
          case "+": System.out.println("\tLDWA\ts,x");
                    System.out.println("\tADDA\t" + ctx.NUM() + ",i");
                    System.out.println("\tSTWA\ts,x");
                    break;
          default:  System.out.println("\tLDWA\t" + ctx.NUM() + ",i");
                    System.out.println("\tSTWA\t s,x");
                    break;
        }
  }


  @Override
  public void enterAddOp(MountCParser.AddOpContext ctx) {
        //System.out.println("\tSUBSP\t2,i");
        //System.out.println("\tSTWA\t0,s");

  }

  @Override
  public void enterMinusOp(MountCParser.MinusOpContext ctx) {
    //System.out.println("\tSUBSP\t2,i");
    //System.out.println("\tSTWA\t0,s");
  }

  @Override
  public void exitOpExpr(MountCParser.OpExprContext ctx) {
      //System.out.println("\tADDA\t0,s");
      //System.out.println("\tADDSP\t2,i");
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
    System.out.println("\tSUBSP\t2,i");
  }

  @Override
  public void exitFunCall(MountCParser.FunCallContext ctx) {
      String id = ctx.getParent().getChild(0).toString();
      System.out.println("\tCALL\t" + id);
      int numBytesToPop = 2*(symtab.get(id) + 1);  //  Won't get here if id not in symtab.
      System.out.println("\tADDSP\t" + numBytesToPop + ",i");
      //System.out.println(ctx.getParent().getParent().getParent().getChild(0).getChild(0).toString());
      String op = ctx.getParent().getParent().getParent().getChild(0).getChild(0).toString();
      switch(op){
        case "-": System.out.println("\tLDWA\ts,x");
                  System.out.println("\tSUBA\t-2,s");
                  System.out.println("\tSTWA\ts,x");
                  System.out.println("\tLDWA\ts,x");
                  break;
        case "+": System.out.println("\tLDWA\ts,x");
                  System.out.println("\tADDA\t-2,s");
                  System.out.println("\tSTWA\ts,x");
                  System.out.println("\tLDWA\ts,x");
                  break;
        default:  System.out.println("\tLDWA\t-2,s");
                  break;
      }
  }

  @Override
  public void exitArgListExpr(MountCParser.ArgListExprContext ctx) {
    System.out.println("\tSUBSP\t2,i");
    System.out.println("\tSTWA\t0,s");
  }

}
