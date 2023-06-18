export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [response, responseUserInfo, responseSemester] = await Promise.all([
		fetch(urlAPI + '/project/semester/getProjects', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/planning/semester', {
			method: 'GET',
			headers: headersList
		})
	]);
	if (response.status === 401) {
		return { status: 401 };
	}
	const projects = await response.json();
	const userInfo = await responseUserInfo.json();
	const semester = await responseSemester.json();

	return { projects, userInfo,semester, status: 200 };
};
