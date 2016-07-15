package ch.scotty.job

trait Job[J] {
  def run(jobConfiguration : J): Unit
}
