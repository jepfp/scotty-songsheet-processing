package ch.scotty.job.json

import ch.scotty.job.determinesongstoconvert.DetermineSongsToConvertJobConfiguration

case class JobDefinitions(
  singleSongToImageConverterJob: Option[Seq[SingleSongToImageConverterJobConfiguration]],
  allSongToImageConverterJob: Option[Seq[AllSongToImageConverterJobConfiguration]],
  determineSongsToConvertJob: Option[Seq[DetermineSongsToConvertJobConfiguration]]
)
