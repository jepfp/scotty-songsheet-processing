package ch.scotty.job.json.result

import java.io.File
import java.util.UUID

import ch.scotty.{ResourceFileContentWithFileContentComparator, UnitSpec}

class JobResultWriterTest extends UnitSpec with TestFolder {

  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "The JobResultWriter" should "write the results to a json as expected" in {
    // arrange
    val jobResults = Map(//
      "job1" -> new PerJobDefinitionResultHolder(//
        Seq(Success(UUID.fromString("cc07c39c-f160-11e6-bc64-92361f002671"))), //
        Seq() //
      ), //
      "job2" -> new PerJobDefinitionResultHolder(//
        Seq(Success(UUID.fromString("cc07c6da-f160-11e6-bc64-92361f002671"))), //
        Seq(Failure(UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002671"), "first error message...", Seq(new Exception("with one exception"))), //
          Failure(UUID.fromString("e2cb5eb8-f160-11e6-bc64-92361f002671"), "second error message...", Seq(new Exception("with two exceptions..."), new Exception("see?")))))
    )
    val outputFile = new File(testFolder, "actualJobResults.json")
    // act
    JobResultWriter.writeJobResults(outputFile, jobResults)
    // assert
    assertContentEquals("expectedResult.json", outputFile)
  }
}
