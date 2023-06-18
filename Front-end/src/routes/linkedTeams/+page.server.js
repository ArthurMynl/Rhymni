export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [responsePair, responseTeam, responseUserInfo] = await Promise.all([
		fetch(urlAPI + '/optionLeader/getTeamPair', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/team/getUnpairedTeams', {
			method: 'GET',
			headers: headersList
		}),
		fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: headersList
		})
	]);
	if (responsePair.status === 401 || responseTeam.status === 401) {
		return { status: 401 };
	}
	const userInfo = await responseUserInfo.json();
	const pairData = await responsePair.json();
	const teamData = await responseTeam.json();
	return { pairData, teamData, userInfo, status: 200 };
};
