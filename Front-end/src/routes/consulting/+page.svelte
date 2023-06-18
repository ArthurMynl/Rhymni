<script>
	const urlAPI = process.env.URLAPI;
	import Calendar from '@event-calendar/core';
	import TimeGrid from '@event-calendar/time-grid';
	import SidePanelConsulting from './SidePanelConsulting.svelte';

	import ConsultingImport from './ConsultingImport.svelte';
	import Cookies from 'js-cookie';
	import Navbar from '../Navbar.svelte';
	import { toast } from '@zerodevx/svelte-toast';

	export let data;
	let consultingsPlannedData = data.consultingsAllPlannedData;
	let specialityData = data.allSpecData;
	let userInfo = data.userInfo;
	let planningList = data.allPlanningData;
	let availableSlots = data.availableSlots;
	let allTeachers = data.allTeachersData;
	let studentConnected = data.studentConnected;
	$: showSidePanel = false;

	console.log(data);
	const planingStartHour = '08:00:00';
	const planingEndHour = '18:00:00';
	const consultingDuration = 20;
	const slotDuration = '00:' + consultingDuration;
	let specialitySelectedName = specialityData[0].name;

	/**
	 * TODO: modify the colors
	 */
	const consultingPlannedColor = 'rgba(235,78,63)';
	const planningAvailableColor = 'rgba(0,166,107)';
	const planningUnavailableColor = 'rgba(137,148,150)';
	const consultingWaitingColor = 'black';

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	let eventsBySpeciality = [];
	for (let k = 0; k < specialityData.length; k++) {
		eventsBySpeciality.push([]);
	}

	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	// let eventSelected = planningList[0];

	/**
	 * Method that returns the index of the speciality in the list given in data
	 * from the speciality name
	 * Used to not hardcode speciality
	 * @param specName
	 */
	function getIndexOfSpeciality(specName) {
		let index = -1;
		for (let i = 0; i < specialityData.length; i++) {
			if (specialityData[i].name == specName) {
				index = i;
			}
		}
		if (index != -1) {
			return index;
		} else {
			return console.error('Speciality name not found');
		}
	}

	/**
	 * Get the index of the planning in the planningList
	 * @param id of the planning to find
	 */
	function getIndexInPlanningList(id) {
		for (let i = 0; i < planningList.length; i++) {
			if (planningList[i].idPlanning == id) {
				return i;
			}
		}
	}

	/**
	 * Says if the planning is in the eventBySpeciality list
	 * @param spec : speciality name slected in the planning
	 * @param id : id of the planning
	 * @returns {boolean} : true if the planning is in the list, false otherwise
	 */
	function idIsInList(spec, id) {
		let list = eventsBySpeciality[getIndexOfSpeciality(spec)];
		for (let i = 0; i < list.length; i++) {
			if (list[i].id == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the teachers info (used to get his speciality)
	 * @param teacherID
	 */
	function getTeacherByID(teacherID) {
		let teacherRet;
		for (let k = 0; k < allTeachers.length; k++) {
			if (allTeachers[k].id == teacherID) {
				teacherRet = allTeachers[k];
			}
		}
		return teacherRet;
	}

	/**
	 * Get the planning JSON
	 * @param planningID
	 */
	function getPlanningByID(planningID) {
		for (let i = 0; i < planningList.length; i++) {
			if (planningList[i].idPlanning === planningID) {
				return planningList[i];
			}
		}
		return null;
	}

	/**
	 * Get the teacher list of all the teachers with the right speciality
	 * @param specName
	 */
	function getTeachersOfSpeciality(specName) {
		let teachers = [];
		for (let i = 0; i < allTeachers.length; i++) {
			for (let j = 0; j < allTeachers[i].specialities.length; j++) {
				if (allTeachers[i].specialities[j].nameSpeciality === specName) {
					teachers.push(allTeachers[i]);
				}
			}
		}
		return teachers;
	}

	$: teacherAvailableList = allTeachers;

	/**
	 * Get the teachers list of all the teachers available in the event selected
	 * for the right speciality
	 */
	function getTeachersAvailableForEventSelectedInSpeciality(specName) {
		let teachers = [];
		let teachersOfSpeciality = getTeachersOfSpeciality(specName);
		for (let i = 0; i < eventSelected.teachersId.length; i++) {
			for (let j = 0; j < teachersOfSpeciality.length; j++) {
				if (eventSelected.teachersId[i] === teachersOfSpeciality[j].id) {
					teachers.push(teachersOfSpeciality[j]);
				}
			}
		}
		return teachers;
	}

	/**
	 * Method to get the color for an event depending on it's idPlanning
	 * @param idPlanning
	 * @returns {string} : color rgba(r,g,b,a) for the planning on the calendar
	 */
	function getColorForEvent(idPlanning) {
		// TODO : Mettre les couleurs non harcodées
		// TODO : JURY MEMBER
		switch (userInfo.role) {
			case 'ROLE_OPTION_LEADER':
			case 'ROLE_TEACHER':
			case 'ROLE_JURY_MEMBER':
				if (getPlanningByID(idPlanning).teachersID.includes(userInfo.userId)) {
					// If teacher connected is available on this planning
					return planningAvailableColor;
				} else {
					return planningUnavailableColor;
				}
			case 'ROLE_TEAM_MEMBER' || 'ROLE_STUDENT':
				return planningAvailableColor;
		}
	}

	/**
	 * Init all events displayed on the calendar component, sorted by speciality
	 * The events are displayed in the right speciality if at least one teacher
	 * of this speciality is available in this slot
	 */
	function initEventsBySpecialityList() {
		// let indxTaknByTeam = getConsultingPlanningIndexForStudentTeam();
		// Add all consulting planned by the team to the calendar
		if (userInfo.role == 'ROLE_TEAM_MEMBER' || userInfo.role == 'ROLE_STUDENT') {
			let teamConsulting = initConsultingsForTeam(studentConnected.idTeam);
			for (let i = 0; i < teamConsulting.length; i++) {
				let etendedPops = {
					isAssigned: false,
					isConsulting: true,
					idConsulting: teamConsulting[i].idConsulting
				};
				let consulting = {
					id: teamConsulting[i].idPlanning,
					start: new Date(getPlanningByID(teamConsulting[i].idPlanning).dateStart),
					end: new Date(
						new Date(getPlanningByID(teamConsulting[i].idPlanning).dateStart).getTime() +
							consultingDuration * 60000
					),
					title: teamConsulting[i].speciality
				};
				if (teamConsulting[i].idTeacher === -1) {
					consulting.backgroundColor = consultingWaitingColor;
				} else {
					etendedPops.isAssigned = true;
					consulting.backgroundColor = consultingPlannedColor;
				}
				consulting.extendedProps = etendedPops;
				eventsBySpeciality[getIndexOfSpeciality(teamConsulting[i].speciality)].push(consulting);
				// delete slot available for consulting slot
				getIndexInPlanningList(teamConsulting[i].idPlanning);
			}

			// All available slots with at least one teacher available (all specialities)
			for (let idxSlot = 0; idxSlot < availableSlots.length; idxSlot++) {
				// Iterates all teachers available for this slot
				for (let indxTchr = 0; indxTchr < availableSlots[idxSlot].teachersID.length; indxTchr++) {
					// Iterates all specialities of this teacher
					let specList = getTeacherByID(availableSlots[idxSlot].teachersID[indxTchr]).specialities;
					for (let idxTchrSpc = 0; idxTchrSpc < specList.length; idxTchrSpc++) {
						if (
							!idIsInList(specList[idxTchrSpc].nameSpeciality, availableSlots[idxSlot].idPlanning)
						) {
							eventsBySpeciality[getIndexOfSpeciality(specList[idxTchrSpc].nameSpeciality)].push({
								id: availableSlots[idxSlot].idPlanning,
								start: new Date(availableSlots[idxSlot].dateStart),
								backgroundColor: planningAvailableColor,
								end: new Date(
									new Date(availableSlots[idxSlot].dateStart).getTime() + consultingDuration * 60000
								),
								title: ''
							});
						}
					}
				}
			}
			return eventsBySpeciality[getIndexOfSpeciality(specialitySelectedName)];
		} else {
			return initEventsForTeachers();
		}
	}

	function initEventsForTeachers() {
		let events = [];
		let idPlanningForConsulting = [];

		if (
			userInfo.role === 'ROLE_TEACHER' ||
			userInfo.role === 'ROLE_JURY_MEMBER' ||
			userInfo.role === 'ROLE_PLANNING_ASSISTANT' ||
			userInfo.role === 'ROLE_OPTION_LEADER'
		) {
			// Iterates all consulting registered in DB
			for (let i = 0; i < consultingsPlannedData.length; i++) {
				// If the consulting is assigned to the teacher connected or (is unassigned and an option leader is connected)
				if (
					consultingsPlannedData[i].idTeacher === userInfo.userId ||
					(consultingsPlannedData[i].idTeacher === -1 && userInfo.role === 'ROLE_OPTION_LEADER')
				) {
					let etendedPops = {
						isAssigned: consultingsPlannedData[i].idTeacher !== -1,
						isConsulting: true,
						idConsulting: consultingsPlannedData[i].idConsulting
					};
					let eventConsulting = {
						id: consultingsPlannedData[i].idPlanning,
						start: new Date(getPlanningByID(consultingsPlannedData[i].idPlanning).dateStart),
						end: new Date(
							new Date(getPlanningByID(consultingsPlannedData[i].idPlanning).dateStart).getTime() +
								consultingDuration * 60000
						),
						title: consultingsPlannedData[i].speciality,
						extendedProps: etendedPops
					};
					if (etendedPops.isAssigned) {
						eventConsulting.backgroundColor = consultingPlannedColor;
					} else {
						eventConsulting.backgroundColor = consultingWaitingColor;
					}
					events.push(eventConsulting);
					// if (!consultingsPlannedData[i].idTeacher === -1) {
					// 	idPlanningForConsulting.push(consultingsPlannedData[i].idPlanning);
					// }
					idPlanningForConsulting.push(consultingsPlannedData[i].idPlanning);
				}
			}
			for (let j = 0; j < planningList.length; j++) {
				if (!idPlanningForConsulting.includes(planningList[j].idPlanning)) {
					let planning = planningList[j];
					let eventAllPlanning = {
						id: planning.idPlanning,
						start: new Date(planning.dateStart),
						end: new Date(new Date(planning.dateStart).getTime() + consultingDuration * 60000),
						backgroundColor: getColorForEvent(planning.idPlanning),
						title: ''
					};
					events.push(eventAllPlanning);
				}
			}
		}
		return events;
	}

	/**
	 * Method that sort the list with all consulting to return only
	 * those for the team of the student connected
	 * @param idTeam of the student
	 * @returns consulting list of all consultings for this team
	 */
	function getAllConsultingsForTeam(idTeam) {
		let consultings = [];
		for (let i = 0; i < consultingsPlannedData.length; i++) {
			if (consultingsPlannedData[i].idTeam === idTeam) {
				consultings.push(consultingsPlannedData[i]);
			}
		}
		return consultings;
	}

	function initConsultingsForTeam(idTeam) {
		let consultings = getAllConsultingsForTeam(idTeam);
		let events = [];
		for (let i = 0; i < consultings.length; i++) {
			events.push({
				idConsulting: consultings[i].idConsulting,
				idPlanning: consultings[i].idPlanning,
				idTeacher: consultings[i].idTeacher,
				idTeam: consultings[i].idTeam,
				review: consultings[i].review,
				speciality: consultings[i].speciality
			});
		}
		return events;
	}

	let calendarRef;
	let plugins = [TimeGrid];
	let options = {
		view: 'timeGridWeek',
		slotDuration: slotDuration,
		slotMinTime: planingStartHour,
		slotMaxTime: planingEndHour,
		hiddenDays: [0, 6],
		events: initEventsBySpecialityList(),
		eventClick: handleEventClick,
		noEventsClick: handleNoEventClick
	};

	$: eventSelected = {
		idPlanning: -1,
		dateStart: null,
		teachersId: [],
		isConsulting: false,
		idConsulting: -1,
		title: '',
		isAssigned: false,
		review: '',
		speciality: '',
		slotDuration: slotDuration
	};
	function handleEventClick(info) {
		// eventSelected = planningList[getIndexInPlanningList(info.event.id)];
		eventSelected.idPlanning = planningList[getIndexInPlanningList(info.event.id)].idPlanning;
		eventSelected.dateStart = planningList[getIndexInPlanningList(info.event.id)].dateStart;
		eventSelected.teachersId = planningList[getIndexInPlanningList(info.event.id)].teachersID;
		eventSelected.speciality = specialitySelectedName;

		eventSelected.isConsulting = info.event.extendedProps.isConsulting;
		if (info.event.extendedProps.isConsulting) {
			eventSelected.title = info.event.title;
			eventSelected.speciality = eventSelected.title;
			for (let i = 0; i < consultingsPlannedData.length; i++) {
				if (consultingsPlannedData[i].idPlanning == info.event.id) {
					eventSelected.review = consultingsPlannedData[i].review;
					eventSelected.idConsulting = info.event.extendedProps.idConsulting;
					eventSelected.isAssigned = info.event.extendedProps.isAssigned;
				}
			}
		}
		teacherAvailableList = getTeachersAvailableForEventSelectedInSpeciality(
			eventSelected.speciality
		);
		showSidePanel = true;
		if (
			userInfo.role === 'ROLE_TEACHER' ||
			userInfo.role === 'ROLE_JURY_MEMBER' ||
			userInfo.role === 'ROLE_OPTION_LEADER'
		) {
			// displayButtonDispoPlanning();
			// displayInfoConsulting();
		}
	}

	function planningClickedIsConsulting(eventClicked) {
		let idPlann = eventClicked.idPlanning;
		let allConsultingsForTeam = getAllConsultingsForTeam(userInfo.idEquipe);
		for (let i = 0; i < allConsultingsForTeam.length; i++) {
			if (
				allConsultingsForTeam[i].idPlanning === idPlann &&
				allConsultingsForTeam[i].speciality === specialitySelected
			) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method called when the user dont click on an event (?)
	 * @param info
	 * TODO: FIX
	 */
	function handleNoEventClick(info) {
		showSidePanel = false;
	}

	/**
	 * Method used to switch the slots available depending on the speciality selected by the user
	 * @param index : index of the speciality selected
	 */
	function changeCalendarSpeciality(specName) {
		specialitySelectedName = specName;
		options.events = eventsBySpeciality[getIndexOfSpeciality(specName)];
	}

	/**
	 * Method that refresh the calendar
	 */
	async function refreshData() {
		let responseConsultingsPlanned = await fetch(urlAPI + '/consulting/getAllPlanned', {
			method: 'GET',
			headers: headersList
		});

		const responsePlanningList = await fetch(urlAPI + `/planning/getPlanning`, {
			method: 'GET',
			headers: headersList
		});

		const responseSpecialty = await fetch(urlAPI + '/consulting/getSpecialities', {
			method: 'GET',
			headers: headersList
		});

		const responseAvailableSlotsList = await fetch(urlAPI + '/consulting/getAllAvailableSlots', {
			method: 'GET',
			headers: headersList
		});

		const responseAllTeachers = await fetch(urlAPI + '/teacher/getTeacher', {
			method: 'GET',
			headers: headersList
		});

		consultingsPlannedData = await responseConsultingsPlanned.json();
		specialityData = await responseSpecialty.json();
		planningList = await responsePlanningList.json();
		allTeachers = await responseAllTeachers.json();
		availableSlots = await responseAvailableSlotsList.json();

		options.events = initEventsBySpecialityList();
	}

	function removeDisponibility() {
		refreshData();
		toast.push('Disponibilité retirée', {
			theme: {
				'--toastBackground': '#eb4e3f',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			duration: 2000,
			pausable: true
		});
	}

	function addDisponibility() {
		refreshData();
		toast.push('Disponibilité ajoutée !', {
			theme: {
				'--toastBackground': '#00a66b',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			duration: 2000,
			pausable: true
		});
	}

	function askConsulting() {
		refreshData();
		toast.push('Consulting demandé !', {
			theme: {
				'--toastBackground': '#3366ff',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}
</script>

<Navbar {userInfo} />
<div class="container">
	<div id="calendar-div">
		<Calendar bind:this={calendarRef} {plugins} {options} />
	</div>

	<div class="side-panel">
		{#if userInfo.role === 'ROLE_TEAM_MEMBER'}
			<div class="select-spec">
				<h1>Selectionner une spécialité</h1>
				<div class="btn-spec-group">
					{#each specialityData as spec}
						<button
							id={spec.name}
							on:click={() => changeCalendarSpeciality(spec.name)}
							class:selected={specialitySelectedName === spec.name}
						>
							{#if spec.name === 'INF'}
								Infrastructure
							{:else if spec.name === 'DEV'}
								Développement
							{:else if spec.name === 'MOD'}
								Modélisation
							{/if}
						</button>
					{/each}
				</div>
			</div>
		{/if}
		{#if userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
			<ConsultingImport />
		{/if}
		{#if showSidePanel}
			{#if userInfo.role === 'ROLE_PLANNING_ASSISTANT' || userInfo.role === 'ROLE_TEAM_MEMBER'}
				<hr />
			{/if}
			<SidePanelConsulting
				event={eventSelected}
				{headersList}
				allTeachers={teacherAvailableList}
				{userInfo}
				{urlAPI}
				specialitySelected={specialitySelectedName}
				isStudent={userInfo.role === 'ROLE_TEAM_MEMBER'}
				on:removeDispo={removeDisponibility}
				on:addDispo={addDisponibility}
				on:askConsulting={askConsulting}
				on:assignTeacher={refreshData}
			/>
		{/if}
	</div>
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: row;
		justify-content: flex-start;
		align-items: flex-start;
		margin-bottom: 50px;
		margin-top: 10px;

		#calendar-div {
			width: 74%;
			margin-left: 1%;
		}
		.side-panel {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 25%;
			height: 100%;
			margin-top: 10px;

			hr {
				color: #c4c4c4;
				width: 80%;
				margin: 40px;
			}

			.select-spec {
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				gap: 30px;
				border-radius: 15px;

				.btn-spec-group {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;
					gap: 15px;

					.selected {
						background-color: #0044cc;
						box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
					}

					button {
						padding: 10px;
						text-align: center;
						border-radius: 10px;
						width: 100%;
						color: white;
						background-color: #3366ff;
						border: none;
						font-size: 1em;
					}
				}
			}
		}
	}
</style>
