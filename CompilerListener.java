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
  private int ifIDGenerator = 1;
  public int ifID = 1;

  @Override
  public void enterProgram(MountCParser.ProgramContext ctx) {
    System.out.println("\tSUBSP\t2,i");
    System.out.println("\tCALL\tmain");
    System.out.println("\tADDSP\t2,i");
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
    if(!symtab.containsKey(id)){
      symtab.put(id, 0);
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
      int numBytesToPop = 2*(symtab.get(id) + 1);
      System.out.println("\tADDSP\t" + numBytesToPop + ",i");
      System.out.println("\tLDWA\t-2,s");
  }

  @Override public void exitExprList(MountCParser.ExprListContext ctx) {
    if(ctx.getParent().getChild(0).getChild(0).toString().equals("-")){
      System.out.println("\tNEGA");
    }
    if(ctx.getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class) || ctx.getParent().getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class)){
      System.out.println("\tADDA\t0,s");
      System.out.println("\tADDSP\t2,i");
    }
  }


  @Override
  public void enterExpr_tail(MountCParser.Expr_tailContext ctx) {
      if(!ctx.getParent().getParent().getClass().equals(MountCParser.Expr_tailContext.class)){
          System.out.println("\tSUBSP\t2,i");
          System.out.println("\tSTWA\t0,s");
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
          System.out.println("\tADDSP\t2,i");
      }
  }

  @Override
  public void enterActualArg(MountCParser.ActualArgContext ctx) {
    System.out.println("\tSUBSP\t2,i");
    System.out.println("\tSTWA\t0,s");
  }

  @Override
  public void enterEmptyArglist(MountCParser.EmptyArglistContext ctx) {
    System.out.println("\tSUBSP\t2,i");
    System.out.println("\tSTWA\t0,s");
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
  		ifID = ifID*100;
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
