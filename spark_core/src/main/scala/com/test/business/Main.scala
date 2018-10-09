package com.test.business

object Main extends App {
  var result = List[Int]()
  result = result.::(1)
  result = result.::(2)
  result = result.::(3)
  result = result.::(4)

  println(result)
}
