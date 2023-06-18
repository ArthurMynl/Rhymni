<script>
    import { goto } from '$app/navigation';
    import Cookies from 'js-cookie';
    import { onMount } from 'svelte';
    const urlAPI = process.env.URLAPI;
    
    async function getUserInfo() {
		let response = await fetch(urlAPI + '/auth/getUserInfo', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Bearer ' + Cookies.get('token')
			}
		})
		let data = await response.json();
		return data
	}

	async function redirect() {
		let userInfo = await getUserInfo();
		if (userInfo.role === 'ROLE_STUDENT') {
			goto('/project')
		}
		else if (userInfo.role === "ROLE_TEAM_MEMBER") {
			goto(`/project/${userInfo.idTeam}`)
		}
		else if (userInfo.role === "ROLE_TEACHER" || userInfo.role === 'ROLE_JURY_MEMBER' || userInfo.role === "ROLE_OPTION_LEADER") {
			goto(`/teacher/${userInfo.userId}`)
		}
		else {
			goto('/auth')
		}
	}

    onMount(async () => {
        if (Cookies.get('token')) {
            await redirect()
        }
        else {
            goto('/auth')
        }
    })
</script>