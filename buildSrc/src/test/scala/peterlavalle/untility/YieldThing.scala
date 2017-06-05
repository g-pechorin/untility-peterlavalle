package peterlavalle.untility

/**
  * I needed to learn (and remind myself) how `yield` worked
  */
object YieldThing extends App {
  for (
    version <- List("/5.1.4f1", "/5.6.0f3", "");
    folder <- List("Program Files (x86)", "Program Files");
    drive <- 'A' to 'B'
  ) println(s"$drive:/$folder/Unity$version/Editor/Unity.exe")
}
