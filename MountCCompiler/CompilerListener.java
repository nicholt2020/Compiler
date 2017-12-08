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
