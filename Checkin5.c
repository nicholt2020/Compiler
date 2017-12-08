foo(a, b){
   return (a - 7)
          ? putchar(getchar())
          : putint(getint()),
          ;
}

main(){
  return foo(getint(), getchar());
}
