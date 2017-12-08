foo(a, b){
   return (a - b)
          ? putchar(77)
          : putint(777),
          ;
}

main(){
  return foo(getint(), getchar());
}
