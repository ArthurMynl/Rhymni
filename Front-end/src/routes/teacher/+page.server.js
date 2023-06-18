export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [response, reponseUserInfo] = await Promise.all([
		fetch(urlAPI + `/teacher/getTeacher`, {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: headersList
		})
	]);

	if (response.status === 401) {
		return { status: 401 };
	}

	const teachers = await response.json();
	const userInfo = await reponseUserInfo.json();

	return { teachers, userInfo, status: 200 };
};
