package org.saudigitus.entry_points.data.remote.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.saudigitus.entry_points.data.model.Patient
import org.saudigitus.entry_points.data.model.Prescription
import org.saudigitus.entry_points.data.model.put.DataValue
import org.saudigitus.entry_points.data.model.put.UpdateEvent
import org.saudigitus.entry_points.data.model.response.OptionResponse
import org.saudigitus.entry_points.data.model.response.TrackedEntityInstanceResponse
import org.saudigitus.entry_points.data.remote.repository.PrescriptionRepository
import org.saudigitus.entry_points.network.BaseNetwork
import org.saudigitus.entry_points.network.HttpClientHelper
import org.saudigitus.entry_points.network.NetworkUtils
import org.saudigitus.entry_points.network.URLMapping.optionsUrl
import org.saudigitus.entry_points.network.URLMapping.putEventUrl
import org.saudigitus.entry_points.network.URLMapping.teiAttributesUrl
import org.saudigitus.entry_points.network.URLMapping.teiEventsUrl
import org.saudigitus.entry_points.network.URLMapping.teiRelationshipUrl
import org.saudigitus.entry_points.utils.UIDMapping
import org.saudigitus.entry_points.utils.UIDMapping.attributes
import org.saudigitus.entry_points.utils.getByAttr

class PrescriptionRepositoryImpl(
    override val networkUtil: NetworkUtils,
    httpClientHelper: HttpClientHelper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseNetwork(httpClientHelper.httpClient(), networkUtil), PrescriptionRepository {
    override suspend fun savePrescription(
        tei: String,
        ou: String,
        event: String,
        dataElement: String,
        value: String
    ) = withContext(ioDispatcher) {
        val data = UpdateEvent(
            event = event,
            orgUnit = ou,
            dataValues = listOf(
                DataValue(
                    dataElement = dataElement,
                    value = value.toIntOrNull() ?: 0
                )
            ),
            program = UIDMapping.PROGRAM,
            programStage = UIDMapping.PROGRAM_STAGE,
            status = "ACTIVE",
            trackedEntityInstance = tei,
        )

        val response = put<Unit, UpdateEvent>(
            putEventUrl(event, dataElement),
            data
        ).getOrNull()

        return@withContext response?.first == 200 || response?.first == 201
    }

    override suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ): List<Prescription> = withContext(ioDispatcher) {
        val events = get<TrackedEntityInstanceResponse>(teiEventsUrl(tei, program))
            .getOrNull()
            ?.trackedEntityInstances
            ?.flatMap { it.enrollments }
            ?.flatMap { it.events }
            ?.filter { it.programStage == stage }
            ?: return@withContext emptyList()

        val eventDataMap = events.associateBy(
            { Pair(it.event, it.orgUnit) },
            { it.dataValues }
        )

        return@withContext eventDataMap.keys.map { key ->
            val dataValues = eventDataMap[Pair(key.first, key.second)] ?: emptyList()

            val optionCode = dataValues.firstOrNull { it.dataElement == UIDMapping.DATA_ELEMENT_NAME }?.value
            val posology = dataValues.firstOrNull { it.dataElement == UIDMapping.DATA_ELEMENT_POSOLOGY }?.value
            val requestedQtd = dataValues.firstOrNull { it.dataElement == UIDMapping.DATA_ELEMENT_QTD_REQ }
                ?.value?.toIntOrNull() ?: 0
            val completedQtd = dataValues.firstOrNull { it.dataElement == UIDMapping.DATA_ELEMENT_QTD_GIVEN }
                ?.value?.toIntOrNull() ?: 0

            val name = get<OptionResponse>(optionsUrl(optionCode.orEmpty()))
                .getOrNull()
                ?.options
                ?.find { it.code == optionCode }
                ?.name.orEmpty()

            Prescription(
                uid = key.first,
                ou = key.second,
                name = name,
                posology = posology.orEmpty(),
                requestedQtd = requestedQtd,
                completedQtd = completedQtd,
                isCompleted = true
            )
        }
    }

    override suspend fun getPatient(
        uid: String,
        program: String,
        relationshipType: String
    ): Patient? = withContext(ioDispatcher) {
        val relationship = async {
            get<TrackedEntityInstanceResponse>(teiRelationshipUrl(uid, UIDMapping.PROGRAM))
                .getOrNull()
                ?.trackedEntityInstances
                ?.flatMap { it.relationships }
                ?.find { it.relationshipType == relationshipType }
        }.await()

        val relatedTei = relationship?.from?.trackedEntityInstance?.trackedEntityInstance.orEmpty()
        if (relatedTei.isEmpty()) return@withContext null

        val response = get<TrackedEntityInstanceResponse>(teiAttributesUrl(relatedTei, program))
        val teiData = response.getOrNull() ?: return@withContext null

        val attributes = teiData.trackedEntityInstances
            .flatMap { it.attributes }
            .filter { it.attribute in attributes() }

        return@withContext Patient(
            uid = uid,
            name = attributes.getByAttr(UIDMapping.NAME_ATTR),
            surname = attributes.getByAttr(UIDMapping.SURNAME_ATTR),
            birthdate = attributes.getByAttr(UIDMapping.BIRTHDATE_ATTR),
            residence = attributes.getByAttr(UIDMapping.RESIDENCE_ATTR),
            gender = attributes.getByAttr(UIDMapping.GENDER_ATTR),
            processNumber = attributes.getByAttr(UIDMapping.PROCESS_NUMBER_ATTR),
        )
    }
}