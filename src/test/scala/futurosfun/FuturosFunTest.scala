package futurosfun

import org.scalatest.FunSuite

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalaz.{Failure => zFailure, Success => zSuccess, Validation}

/**
 * Created by abelmeos on 2017/03/16.
 */
class FuturosFunTest extends FunSuite{

  test("prueba futuro") {


    import scala.concurrent.ExecutionContext.Implicits.global

    case class Exception(solicitud:Int)

    def foo(i:Int): Future[Validation[Exception, String]] = {

      Future{

        i%2 match {
          case 0 => zSuccess("PAR")
          case _ => zFailure(Exception(i))
        }

      }

    }

    val lista = (1 to 10).map(x => foo(x)).toList

    val prueba = Future.sequence(lista)

    val futureListOfFailures = prueba.map {

      x => x.filter {
        y => y.isFailure
      }

    }

    futureListOfFailures onComplete {
      case Success (r) =>
        r.map{
          y => y match {
            case zFailure(error) => println(error)
          }
        }

    }

    Thread.sleep(30000)



  }

}
