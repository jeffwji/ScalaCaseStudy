import org.scalameter.api._

/*object Test extends App{
    @inline def inline_inc(a:Int) = a + 1
    @inline def inline_dec(a:Int) = a - 1

    @noinline def inc(a:Int) = a + 1
    @noinline def dec(a:Int) = a - 1

    def timeit[R](name:String, block: => R): R = {
        val t0 = System.nanoTime()
        val result = block    // call-by-name
        val t1 = System.nanoTime()
        println(s"$name:\t\t" + (t1 - t0) /1000+ "ms")
        result
    }

    timeit("map", List.fill(1000000)(10).map(_ +1).map(_-1))
    timeit("view", List.fill(1000000)(10).view.map(_ +1).map(_-1).toList)
    timeit("outline", List.fill(1000000)(10).map(inc).map(dec))
    timeit("view", List.fill(1000000)(10).view.map(_ +1).map(_-1).toList)
    timeit("inline", List.fill(1000000)(10).map(inline_inc).map(inline_dec))
    timeit("view", List.fill(1000000)(10).view.map(_ +1).map(_-1).toList)
}*/

object ListBenchmark extends Bench.LocalTime {
    @inline def inline_inc(a:Int) = a + 1
    @inline def inline_dec(a:Int) = a - 1

    @noinline def inc(a:Int) = a + 1
    @noinline def dec(a:Int) = a - 1

    val listGroups = for {
        i <- Gen.range("stuff")(1, 10, 1)
    } yield List.fill(500000)(i)

    performance of "view" in {
        using(listGroups) curve("List") in {
            _.view.map(_ + 1).map(_-1).toList
        }
    }

    performance of "map" in {
        using(listGroups) curve("List") in {
            _.map(_ + 1).map{_-1}
        }
    }

    performance of "outline" in {
        using(listGroups) curve("List") in {
            _.map(inc).map{dec}
        }
    }

    performance of "inline" in {
        using(listGroups) curve("List") in {
            _.map(inline_inc).map{inline_dec}
        }
    }
}