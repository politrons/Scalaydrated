package politrons.scalaydrated

import org.clapper.classutil.{ClassFinder, ClassInfo}


/**
  * Created by pabloperezgarcia on 04/11/2016.
  */
object ModelInspector {

  final val MODEL_PATH = "politrons.scalaydrated.Model"

  var plugins: Iterator[ClassInfo] = _

  def checkModelConstructor(): Unit = {
    if (plugins == null) {
      val finder = ClassFinder()
      val classes = finder.getClasses
      plugins = ClassFinder.concreteSubclasses(MODEL_PATH, classes)
      plugins.foreach { classUtil =>
        checkModel(classUtil)
      }
    }
  }

  def checkModel(classUtil: ClassInfo): Unit = {
    var zeroParams: Boolean = false
    val clazz = Class.forName(classUtil.name)
    clazz.getDeclaredConstructors.foreach { constructor =>
      if (constructor.getParameterCount == 0) {
        zeroParams = true
      }
    }
    if (!zeroParams) {
      throw new UnsupportedClassVersionError(String.format("Error on class definition %s " +
        "your model need to implement zero params constructor", clazz.getName))
    }
  }
}
