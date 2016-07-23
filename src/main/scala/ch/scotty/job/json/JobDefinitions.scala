package ch.scotty.job.json

case class JobDefinitions(
  singleSongToImageConverterJob: Option[Seq[SingleSongToImageConverterJobConfiguration]],
  allSongToImageConverterJob: Option[Seq[AllSongToImageConverterJobConfiguration]]
)
