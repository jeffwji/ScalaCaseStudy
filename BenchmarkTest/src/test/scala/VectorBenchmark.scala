import org.scalameter.api._

object VectorBenchmark extends Bench.LocalTime {
    @inline def inline_inc(a:Int) = a + 1
    @inline def inline_dec(a:Int) = a - 1

    @noinline def inc(a:Int) = a + 1
    @noinline def dec(a:Int) = a - 1

    val vectorGroups = for {
        i <- Gen.range("stuff")(1, 10, 1)
    } yield Vector.fill(500000)(i)

    performance of "view" in {
        using(vectorGroups) curve("List") in {
            _.view.map(_ + 1).map{_-1}.toList
        }
    }

    performance of "map" in {
        using(vectorGroups) curve("List") in {
            _.map(_ + 1).map{_-1}
        }
    }

    performance of "outline" in {
        using(vectorGroups) curve("List") in {
            _.map(inc).map{dec}
        }
    }

    performance of "inline" in {
        using(vectorGroups) curve("List") in {
            _.map(inline_inc).map{inline_dec}
        }
    }
}