export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};

	let responseConsultingsPlanned = await fetch(urlAPI + '/consulting/getAllPlanned', {
		method: 'GET',
		headers: headersList
	});
	const consultingsAllPlannedData = await responseConsultingsPlanned.json();

	const responseUserInfo = await fetch(urlAPI + '/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

	const [responseTeacherConsulting, responseTeacherPlanning] = await Promise.all([
		fetch(urlAPI + `/consulting/getAllForTeacher/` + userInfo.userId, {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + `/planning/getAllPlanningsTeacher/` + userInfo.userId, {
			method: 'GET',
			headers: headersList
		})
	]);

	const teacherConsulting = await responseTeacherConsulting.json();
	const teacherPlanning = await responseTeacherPlanning.json();

	const reponseAllConsulting = await fetch(urlAPI + '/teacher/getConsultings', {
		method: 'GET',
		headers: headersList
	});
	const allConsulting = await reponseAllConsulting.json();

	const responseSpecialty = await fetch(urlAPI + '/consulting/getSpecialities', {
		method: 'GET',
		headers: headersList
	});
	const allSpecData = await responseSpecialty.json();

	// Check if student
	let studentConnected = null;
	let consultingsForTeam = null;
	if(userInfo.role === 'ROLE_TEAM_MEMBER' || userInfo.role === 'ROLE_STUDENT') {
		const studentConnectedResp = await fetch(urlAPI + '/student/getStudent/' + userInfo.userId, {
				method: 'GET',
				headers: headersList
			});
		
		studentConnected = await studentConnectedResp.json();
	}

	const responsePlanningList = await fetch(urlAPI + `/planning/getPlanning`, {
		method: 'GET',
		headers: headersList
	});
	const allPlanningData = await responsePlanningList.json();

	const responseAvailableSlotsList = await fetch(urlAPI + '/consulting/getAllAvailableSlots', {
		method: 'GET',
		headers: headersList
	});
	const availableSlots = await responseAvailableSlotsList.json();

	const responseAllTeachers = await fetch(urlAPI + '/teacher/getTeacher', {
		method: 'GET',
		headers: headersList
	});
	const allTeachersData = await responseAllTeachers.json();

	return {
		allConsulting,
		consultingsAllPlannedData,
		allTeachersData,
		consultingsForTeam,
		allPlanningData,
		teacherPlanning,
		teacherConsulting,
		allSpecData,
		userInfo,
		availableSlots,
		studentConnected
	};
};

