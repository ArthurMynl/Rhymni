export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [studentsResponse, teamResponse, userInfoResponse] = await Promise.all([
		fetch(urlAPI + '/student/getStudents', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/team/getTeams', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: headersList
		})
	]);
	if (studentsResponse.status === 401 || teamResponse.status === 401) {
		return { status: 401 };
	}

	const dataStudents = await studentsResponse.json();
	const dataTeam = await teamResponse.json();
	const userInfo = await userInfoResponse.json();

	return { dataStudents, dataTeam, userInfo, status: 200 };
};
