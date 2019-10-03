package org.thp.thehive.connector.cortex.controllers.v0

import scala.language.implicitConversions

import io.scalaland.chimney.dsl._
import org.thp.cortex.dto.v0.{InputReportTemplate, OutputReportTemplate}
import org.thp.scalligraph.controllers.Output
import org.thp.scalligraph.models.{Entity, UniMapping}
import org.thp.scalligraph.query.{PublicProperty, PublicPropertyListBuilder}
import org.thp.thehive.connector.cortex.models.ReportTemplate
import org.thp.thehive.connector.cortex.services.ReportTemplateSteps

object ReportTemplateConversion {

  implicit def toOutputReportTemplate(j: ReportTemplate with Entity): Output[OutputReportTemplate] =
    Output[OutputReportTemplate](
      j.into[OutputReportTemplate]
        .withFieldComputed(_.analyzerId, _.workerId)
        .withFieldComputed(_.id, _._id)
        .withFieldComputed(_.content, _.content)
        .transform
    )

  val reportTemplateProperties: List[PublicProperty[_, _]] =
    PublicPropertyListBuilder[ReportTemplateSteps]
      .property("analyzerId", UniMapping.string)(_.rename("workerId").readonly)
      .property("reportType", UniMapping.string)(_.field.readonly)
      .property("content", UniMapping.string)(_.field.updatable)
      .build

  implicit def fromInputReportTemplate(inputReportTemplate: InputReportTemplate): ReportTemplate =
    inputReportTemplate
      .into[ReportTemplate]
      .withFieldComputed(_.workerId, _.analyzerId)
      .withFieldComputed(_.content, _.content)
      .transform
}
