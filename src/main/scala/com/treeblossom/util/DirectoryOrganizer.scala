package com.treeblossom.util

import scala.util.matching.Regex
import java.nio.file.{Files, Paths, Path, StandardCopyOption}
import java.io.File

/*
 * organizes a directory filled with files
 *  into subdirectories
 *  according to name patterns specified in a configuration (text) file
 *    see loadConfig for the format of this file
 * 
 */
object DirectoryOrganizer {

  /*
   * 
   * 
   */
  def main(args: Array[String]): Unit = {
    if (args.length !=3) {
      usage()
      sys.exit(1)
    }
    var configFile = args(0)
    var srcdir = args(1)
    var basedir = args(2)
    //println("source directory: " + srcdir)
    //println("base directory: " + basedir)
    var m = loadConfig(configFile)
    //dumpConfig(m)
    m.keys.foreach{key =>
      m.get(key).head.foreach{regex =>
        findMatchingFiles(srcdir,regex).foreach{
          file =>
          //println("mv " + srcdir + "/" + file.getName() + " " + basedir + "/" + key+"/"  )
          moveFile(srcdir + "/" + file.getName(),  basedir + "/" + key,  file.getName() )
        }
      }
    }
    removeEmptyDirs(basedir)
  }


/*
 * Moves a file from srcFile to destFile. 
 * 
 */
  def moveFile(srcFile:String, destDir:String, fileName: String):Unit = {
    ensureDirExists(destDir)
    Files.move(Paths.get(srcFile),
      Paths.get(destDir + "/" + fileName),
      StandardCopyOption.REPLACE_EXISTING)
  }


  /**
    * 
    */
  def ensureDirExists(dirPath:String):Unit = {
    if(!Files.exists(Paths.get(dirPath)))
      Files.createDirectory(Paths.get(dirPath))
  }

  /**
    * 
    */
  def removeEmptyDirs(dirPath:String): Unit = {
    new File(dirPath).listFiles.filter(_.isDirectory).foreach{
      d => if(d.listFiles.length==0) {
        Files.delete(Paths.get(d.getCanonicalPath))
      }
    }
  }

/*
 * 
 * creates a Map[String, List[Regex]]
 *  from a text file formatted thusly:
 * 
 *     1player_podcast ^1P.*.mp3$
 *     amer_icons ^americanicons.*mp3$
 *     us_of_anxiety ^anxiety.*mp3$
 *     amps_n_axes ^AaA.*mp3$
 *     anthropocene ^S.*mp3$
 * 
 * 
 */
  def loadConfig(cfgFilePath: String): collection.mutable.Map[String, List[Regex]] = {
    var m = collection.mutable.Map[String, List[Regex]]()
    scala.tools.nsc.io.File(cfgFilePath).lines().foreach{
      line => m.put(line.split(" ")(0), List[Regex]())
    }

    scala.tools.nsc.io.File(cfgFilePath).lines().foreach{
      line => m.put(line.split(" ")(0),
        m.get(line.split(" ")(0)).head :+ new Regex(line.split(" ")(1))
      )
    }
    return m
  }

  def dumpConfig(cfgMap: collection.mutable.Map[String, List[Regex]]) :Unit = {
    cfgMap.keys.foreach{key => println(cfgMap.get(key).head) }
  }

/*
 * finds files in dir whose names match regexp. 
 * 
 */
  def findMatchingFilesOrig(dir: String, regexp: Regex): Array[java.io.File]  ={
    return new java.io.File(dir).listFiles
      .filter(file => regexp.findFirstIn(file.getName).isDefined)
  }

  def findMatchingFiles(dir: String, regexp: Regex): Array[java.io.File]  ={
    if(new java.io.File(dir).listFiles!=null)
      return new java.io.File(dir).listFiles
        .filter(file => regexp.findFirstIn(file.getName).isDefined)
    else
      return new Array[java.io.File](0)
  }


  def usage(): Unit =  {
    println("Usage:  DirectoryOrganizer  path-to-config-file path-to-src-dir path-to-dest-base-dir")
  }

}
