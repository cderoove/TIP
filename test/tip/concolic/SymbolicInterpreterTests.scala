package tip.concolic

import java.io.{ByteArrayInputStream, File}
import scala.util.{Failure, Try}

import org.junit.Test
import org.junit.Assert._

import tip.ast._
import tip.interpreter.Interpreter
import tip.InterpreterUtils._


class SymbolicInterpreterTests {

  private def allExamples: List[String] =
    new File("examples").list().toList
      .map("examples/" + _)
      .filter(_.endsWith(".tip"))
      .filter(f => Try(prepare(f)).isSuccess)

  private def requiresInput(n: AstNode): Boolean = {
    var f = false
    val inputFinder = new DepthFirstAstVisitor[Null] {
      override def visit(node: AstNode, arg: Null): Unit = {
        node match {
          case AInput(_) => f = true
          case _         => visitChildren(node, null)
        }
      }
    }
    inputFinder.visit(n, null)
    f
  }

  private def requiresInput(file: String): Boolean =
    requiresInput(prepare(file))

  private def crashesTheInterpreter(p: AProgram): Boolean =
    Try(new Interpreter(p).run()).isFailure

  private def crashesTheInterpreter(file: String): Boolean =
    crashesTheInterpreter(prepare(file))


  @Test
	def haveSameOutput() {
    val files = allExamples.filterNot(requiresInput).filterNot(crashesTheInterpreter)
		for(file <- files) {
		  val p1 = prepare(file)
  		val p2 = prepare(file)
			val cres = new Interpreter(p1).run()
      val interpreter = new SymbolicInterpreter(p2)
      val interpreter.Success(_, sres) = interpreter.run()
			assertEquals(cres, sres)
		}
	}

	@Test
	def haveSameOutputRequiringInput() {
    val in = new ByteArrayInputStream("22\n11".getBytes)
		val file = "tipprograms/symbolic1.tip"
		val p1 = prepare(file)
		val p2 = prepare(file)
		Console.withIn(in)  {
      val interpreter1 = new Interpreter(p1)
      val Failure(interpreter1.ApplicationException(cres)) = Try(interpreter1.run())
		  assertEquals(cres, 42)
	  }
    val interpreter2 = new SymbolicInterpreter(p2)
	  val interpreter2.Failure(_, sres) = interpreter2.run(inputs = List(22,11))
	  assertEquals(sres, "Application exception occurred during program execution, error code: 42")
	}
}
