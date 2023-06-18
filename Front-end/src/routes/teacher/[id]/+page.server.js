export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, params, cookies } = loadEvent;
	const token = cookies.get('token');
	const id = params.id;
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [response, responseUserInfo] = await Promise.all([
		fetch(urlAPI + `/teacher/getTeacher/${id}`, {
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

	const teacher = await response.json();
	const userInfo = await responseUserInfo.json();

	return { teacher, userInfo, status: 200 };
};
