export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;
	const { fetch, params, cookies } = loadEvent;
	const token = cookies.get('token');
	const id = params.id;
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const [responseProject, responseFile, responseReport, responseAnalysis, responseTeam, responseStudents, reponseUserInfo] =
		await Promise.all([
			fetch(urlAPI + `/project/getProject/${id}`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + `/file/getFileCode/Cahier des charges projet ${id}.pdf`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + `/file/getFileCode/Rapport ${id}.pdf`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + `/file/getFileCode/Analyse cahier des charges ${id}.pdf`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/team/getUnpairedTeams', {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + `/student/getStudentsInTeam/${id}`, {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/auth/getUserInfo', {
				method: 'GET',
				headers: headersList
			})
		]);

	if (
		responseProject.status === 401 ||
		responseFile.status === 401 ||
		responseReport.status === 401 ||
		responseTeam.status === 401 ||
		responseStudents.status === 401
	) {
		return { status: 401 };
	}

	const project = await responseProject.json();
	const file = await responseFile.json();
	const fileReport = await responseReport.json();
	const fileAnalysis = await responseAnalysis.json();
	const unpairedTeams = await responseTeam.json();
	const teamStudents = await responseStudents.json();
	const userInfo = await reponseUserInfo.json();

	const teamResponse = await fetch(urlAPI + `/team/getTeamByProject/${id}`, {
		method: 'GET',
		headers: headersList
	});
	const team = await teamResponse.json();

	const linkedTeamResponse = await fetch(urlAPI + `/team/getLinkedTeam/${team.number}`, {
		method: 'GET',
		headers: headersList
	});
	const linkedTeam = await linkedTeamResponse.json();

	const linkedProjectResponse = await fetch(
		urlAPI + `/project/getProjectByTeam/${linkedTeam.idLinkedTeam}`,
		{
			method: 'GET',
			headers: headersList
		}
	);

	const linkedProject = await linkedProjectResponse.json();

	const fileLinkedResponse = await fetch(
		urlAPI + `/file/getFileCode/Cahier des charges projet ${linkedProject.id}.pdf`,
		{
			method: 'GET',
			headers: headersList
		}
	);
	const fileLinked = await fileLinkedResponse.json();



	const reportLinkedResponse = await fetch(
		urlAPI + `/file/getFileCode/Rapport ${linkedProject.id}.pdf`,
		{
			method: 'GET',
			headers: headersList
		}
	);

	const reportLinked = await reportLinkedResponse.json();

	const analysisLinkedResponse = await fetch(
		urlAPI + `/file/getFileCode/Analyse cahier des charges ${linkedProject.id}.pdf`,
		{
			method: 'GET',
			headers: headersList
		}
	);

	const analysisLinked = await analysisLinkedResponse.json();

	let linkedStudents = [];
	if (linkedProject.id !== undefined) {
		const linkedStudentsResponse = await fetch(
			urlAPI + `/student/getStudentsInTeam/${linkedProject.id}`,
			{
				method: 'GET',
				headers: headersList
			}
		);
		linkedStudents = await linkedStudentsResponse.json();
	}

	if (file.fileCode !== '' && fileReport.fileCode !== '') {
		const feedbackResponse = await fetch(urlAPI + `/file/${file.fileCode}/getFeedback`, {
			method: 'GET',
			headers: headersList
		});

		const feedback = await feedbackResponse.json();

		const feedbackReportResponse = await fetch(urlAPI + `/file/${fileReport.fileCode}/getFeedback`, {
			method: 'GET',
			headers: headersList
		});

		const feedbackReport = await feedbackReportResponse.json();

		return {
			project,
			file,
			fileReport,
			fileAnalysis,
			linkedProject,
			linkedTeam,
			team,
			unpairedTeams,
			teamStudents,
			linkedStudents,
			fileLinked,
			reportLinked,
			analysisLinked,
			feedback,
			feedbackReport,
			userInfo,
			status: 200
		};
	}

	else if (file.fileCode !== '') {
		const feedbackResponse = await fetch(urlAPI + `/file/${file.fileCode}/getFeedback`, {
			method: 'GET',
			headers: headersList
		});

		const feedback = await feedbackResponse.json();

		return {
			project,
			file,
			fileReport,
			fileAnalysis,
			linkedProject,
			linkedTeam,
			team,
			unpairedTeams,
			teamStudents,
			linkedStudents,
			fileLinked,
			reportLinked,
			analysisLinked,
			feedback,
			feedbackReport: undefined,
			userInfo,
			status: 200
		};
	}

	else if (fileReport.fileCode !== '') {
		const feedbackReportResponse = await fetch(urlAPI + `/file/${fileReport.fileCode}/getFeedback`, {
			method: 'GET',
			headers: headersList
		});

		const feedbackReport = await feedbackReportResponse.json();

		return {
			project,
			file,
			fileReport,
			fileAnalysis,
			linkedProject,
			linkedTeam,
			team,
			unpairedTeams,
			teamStudents,
			linkedStudents,
			fileLinked,
			reportLinked,
			analysisLinked,
			feedback: undefined,
			feedbackReport,
			userInfo,
			status: 200
		};
	}
	else {
		return {
			project,
			file,
			fileReport,
			fileAnalysis,
			linkedProject,
			linkedTeam,
			team,
			unpairedTeams,
			teamStudents,
			linkedStudents,
			fileLinked,
			reportLinked,
			analysisLinked,
			userInfo,
			feedback: undefined,
			feedbackReport: undefined,
			status: 200
		};
	}
};
