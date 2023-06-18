export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};

	try {
		const [responseStudent, responseUserInfo] = await Promise.all([
			fetch(`${urlAPI}/student/getStudent/${params.id}`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(`${urlAPI}/auth/getUserInfo`, {
				method: 'GET',
				headers: headersList
			})
		]);

		if (responseStudent.status === 401 || responseUserInfo.status === 401) {
			return { status: 401 };
		}

		const student = await responseStudent.json();
		const idTeam = student.idTeam;

		const responseTeam = await fetch(`${urlAPI}/team/getTeamByNumber/${idTeam}`, {
			method: 'GET',
			headers: headersList
		});

		if (responseTeam.status === 401) {
			return { status: 401 };
		}

		const team = await responseTeam.json();
		const userInfo = await responseUserInfo.json();

		return { student, team, userInfo, status: 200 };
	} catch (error) {
		console.error('Error:', error);
		return { status: 500 };
	}
};
