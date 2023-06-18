export const load = async (loadEvent) => {
	const urlAPI = process.env.URLAPI;

	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');

	const headersList = {
		'Authorization': `Bearer ${token}`,
		'Content-Type': 'application/json;charset=UTF-8'		
	}

	//recupere les informations sur l'utilisateur
	const [responseUserInfo] = await Promise.all([		
        fetch(urlAPI + `/auth/getUserInfo`, {
            method: "GET",
            headers: headersList
        })
	]);
	const userInfo = await responseUserInfo.json();	
	const id = userInfo.userId ;
	
	//recupere les notifications qui correspondent a l'utilisateur
	const [responseNotifications] = await Promise.all([		       
		fetch(urlAPI + `/notification/getAllNotificationsForUser/${id}`, {
            method: "GET",
            headers: headersList
        })		
	]);

	if (responseNotifications.status === 401) {
		return { status: 401 }
	}		
	
    const dataNotifications = await responseNotifications.json();

	//recupere les user qui ont envoyé une notification
	let requete ;
	if( userInfo.role === "ROLE_STUDENT" | userInfo.role === "ROLE_TEAM_MEMBER"){
		requete = "/teacher/getTeacher/";
	}
	if(userInfo.role === "ROLE_TEACHER" | userInfo.role === "ROLE_OPTION_LEADER" | userInfo.role === "ROLE_JURY_MEMBER" ){
		requete = "/student/getStudent/";
	}
	const sendersPromises = dataNotifications.map(async function(notification) {
		const responseGetSender = await fetch(urlAPI + requete + `${notification.personneSend}`, {
			method: "GET",
			headers: headersList
		});
		const sender = await responseGetSender.json();
		return sender;
	});
	const dataSenders = await Promise.all(sendersPromises);

    return { dataNotifications, dataSenders, userInfo, status: 200 }
}
