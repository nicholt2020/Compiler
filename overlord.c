draw(dash, value){
    return value
               ? (putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  value = 0, draw(dash, value))
               :  (putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(32), putchar(dash), putchar(10),
                  putchar(32), putchar(32), putchar(dash), putchar(10),
                  value = 1, draw(dash, value));

}


main(){
  return draw(getint(), 1);
}
