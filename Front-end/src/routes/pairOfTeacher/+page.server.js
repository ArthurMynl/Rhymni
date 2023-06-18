export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [pairteacherResponse, teacherResponse, userInfoResponse] = await Promise.all([
		fetch(urlAPI + '/teacher/getPairTeacher', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/teacher/getTeacher', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: headersList
		})
	]);

	if (pairteacherResponse.status === 401 || teacherResponse.status === 401) {
		return { status: 401 };
	}

	const pairTeacherData = await pairteacherResponse.json();
	const teacherData = await teacherResponse.json();
	const userInfo = await userInfoResponse.json();

	return { pairTeacherData, teacherData, userInfo, status: 200 };
};
