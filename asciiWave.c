draw(ASCII, value){
    return value
               ? (putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  value = 0, draw(ASCII, value))
               :  (putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  putchar(32), putchar(32), putchar(ASCII), putchar(10),
                  value = 1, draw(ASCII, value));

}


main(){
  return draw(getint(), 1);
}
