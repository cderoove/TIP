package tip.interpreter

import java.io.ByteArrayInputStream

import org.scalatest.{FunSuite, Matchers}
import tip.InterpreterUtils._


class InterpreterTests extends FunSuite with Matchers {
	
	test("factorialRecursiveIterative") {
	  val in = new ByteArrayInputStream("5\n4".getBytes)
		val recursive = "tipprograms/factorial_iterative.tip"
		val iterative = "tipprograms/factorial_recursive.tip"
		val progrec = prepare(recursive)
		val progite = prepare(iterative)
		Console.withIn(in)  {
		  val resrec = new Interpreter(progrec).run()
			resrec shouldBe 120
		  val resite = new Interpreter(progite).run()
			resite shouldBe 24
	  }
	}
	
	test("pointerManipulation") {
	  val p = prepare("tipprograms/pointers.tip")
		new Interpreter(p).run() shouldBe 1
	}
	
	test("map") {
	  val p = prepare("tipprograms/map.tip")
		new Interpreter(p).run() shouldBe 42
	}
	
	test("errorStatement") {
		an[RuntimeException] should be thrownBy {
			val p = prepare("tipprograms/error.tip")
			new Interpreter(p).run()
		}
	}
}
