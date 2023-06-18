<script>
	const urlAPI = process.env.URLAPI;
	let email;
	let password;
    let error = false;

	import Cookies from 'js-cookie';
	import { goto } from '$app/navigation';

	const login = async () => {
		let data = await fetch(urlAPI + '/auth/login', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				login: email,
				password
			})
		});
		if (data.status === 200) {
			let response = await data.json();
			// write to a cookie
			Cookies.set('token', response.accessToken);
            redirect()
		} else {
			error = true
		}
		password = '';
	};

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
			goto('/student')
		}
	}
</script>


<div class="card-container">
	<h1>Se connecter</h1>
	<form on:submit|preventDefault={login}>
        <p class="info">Adresse mail</p>
		<input type="text" bind:value={email} placeholder="prénom.nom@reseau.eseo.fr" />
		<p class="info">Mot de passe</p>
        <input type="password" bind:value={password} placeholder="••••••••" />
        {#if error}
            <p class='error' >L'adresse mail ou le mot de passe est incorrect</p>
        {/if}
		<button id="btn-submit" type="submit">Connexion</button>
	</form>
</div>

<style lang="scss">
	.card-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		border-radius: 25px;
		background-color: white;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
        width: 70%;
        margin: 10% 15%;
        padding: 70px 30px;

		h1 {
            margin: 10px 0;
		}
		form {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 70%;
		}

        .info {
            font-size: 1.2rem;
            font-family: "lato", "sans-serif";
            align-self: flex-start;
            margin-top: 15px;
            margin-bottom: 5px;
            margin-left: 10px;
        }

        .error {
            color: red;
            margin-top: 10px;
            font-family: "lato", "sans-serif";
        }

		input {
			padding: 10px;
			border: 1px solid #ccc;
			border-radius: 10px;
			width: 100%;
		}
		button {
			border: 1px solid #ccc;
			border-radius: 10px;
			cursor: pointer;
			color: white;
			background-color: #3366ff;
            width: 100%;
            font-size: 1.2rem;
            margin-top: 30px;
            padding: 10px;

            &:hover {
                background-color: #7144ec;
            }
		}
	}
</style>
