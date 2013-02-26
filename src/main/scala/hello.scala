object hello {
  def strcat(args: Array[java.lang.String], delimiter: java.lang.String): java.lang.String = {
     args.mkString(delimiter)
  }
  def main(args: Array[java.lang.String]) = {
    println("Hello "+strcat(args, ":")+" World.")
  }
}
