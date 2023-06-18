export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [presentationResponse, roomResponse, pairteacherResponse, TeamResponse] = await Promise.all(
		[
			fetch(urlAPI + '/planning/getPresentations', {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/planning/getRooms', {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/teacher/getPairTeacher', {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/team/getTeams', {
				method: 'GET',
				headers: headersList
			})
		]
	);

	const responseUserInfo = await fetch(urlAPI + '/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

	if (presentationResponse.status === 401) {
		return { status: 401 };
	}

	const dataPresentation = await presentationResponse.json();
	const dataRoom = await roomResponse.json();
	const pairTeacherData = await pairteacherResponse.json();
	const dataTeam = await TeamResponse.json();

	return { dataPresentation, dataRoom, pairTeacherData, dataTeam, userInfo, status: 200 };
};