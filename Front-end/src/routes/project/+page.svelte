<script>
	const urlAPI = process.env.URLAPI;
	export let data;
	import { goto } from '$app/navigation';
	import Cookies from 'js-cookie';
	import Navbar from '../Navbar.svelte';
	import { toast } from '@zerodevx/svelte-toast';
	import { invalidateAll } from '$app/navigation';
	let projects = data.projects;
	let semester = data.semester;
	$: userInfo = data.userInfo;
	$: modeStartDate = false;
	$: modeEndDate = false;
	let dateStart;
	let dateEnd;

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	async function addStudentInTeam(projectId) {
		const newTokenResponse = await fetch(urlAPI + `/teamMember/registerStudent`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				idTeam: projectId
			})
		});
		const newToken = await newTokenResponse.json();
		Cookies.set('token', newToken.accessToken);
		invalidateAll();
		let project = projects.find((project) => project.id === projectId);
		toast.push(`Vous avez rejoint l'équipe ${project.name}`, {
			theme: {
				'--toastBackground': '#00a66b',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}

	async function removeStudentInTeam(projectId) {
		const newTokenResponse = await fetch(urlAPI + `/teamMember/removeStudent`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				idTeam: projectId
			})
		});
		const newToken = await newTokenResponse.json();
		Cookies.set('token', newToken.accessToken);
		invalidateAll();
		let project = projects.find((project) => project.id === projectId);
		toast.push(`Vous avez quitté l'équipe ${project.name}`, {
			theme: {
				'--toastBackground': '#eb4e3f',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}

	function updateStartDate() {
		if (dateStart !== undefined) {
			console.log(dateStart);
			fetch(urlAPI + '/planning/updateStartProject', {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					semesterId: semester.idSemesterInfo,
					startDate : dateStart
				})
			});
			semester.startDate = dateStart;
			modifModeStartDate();
		} else {
			console.log(dateStart);
			modifModeStartDate();
		}
	}

	function updateEndDate() {
		if (dateEnd !== undefined) {
			fetch(urlAPI + '/planning/updateEndProject', {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					semesterId: semester.idSemesterInfo,
					endDate : dateEnd
				})
			});
			semester.endDate = dateEnd;
			modifModeEndDate();
		} else {
			modifModeEndDate();
		}
	}

	function modifModeStartDate() {
		modeStartDate = !modeStartDate;
	}
	function modifModeEndDate() {
		modeEndDate = !modeEndDate;
	}
</script>

<Navbar {userInfo} />
{#if data.userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
	<div class="date-project-card">
		{#if modeStartDate}
			<div class="name-status">
				<p>Changer la date de début des projets</p>
				<input class="input-text" type="date" placeholder={semester.startDate} bind:value={dateStart} />
				<button class="smallButton" on:click={updateStartDate}>Valider</button>
			</div>
		{:else}
			<div class="name-status">
				<p>Changer la date de début des projets</p>
				<h1>{semester.startDate}</h1>
				<button class="smallButton" on:click={modifModeStartDate}>Modifier</button>
			</div>
		{/if}

		{#if modeEndDate}
			<div class="name-status">
				<p>Changer la date de fin des projets</p>
				<input class="input-text" type="date" placeholder={semester.endDate} bind:value={dateEnd} />
				<button class="smallButton" on:click={updateEndDate}>Valider</button>
			</div>
		{:else}
			<div class="name-status">
				<p>Changer la date de fin des projets</p>
				<h1>{semester.endDate}</h1>
				<button class="smallButton" on:click={modifModeEndDate}>Modifier</button>
			</div>
		{/if}
	</div>
{:else}
	<div class="date-project-card">
			<div class="name-status">
				<p>La date de début des projets</p>
				<h1>{semester.startDate}</h1>
			</div>
			<div class="name-status">
				<p>La date de fin des projets</p>
				<h1>{semester.endDate}</h1>
			</div>
	</div>
{/if}
<div class="project-list">
	{#each projects as project}
		<div class="project-card">
			<h3>{project.name}</h3>
			<p>{project.description}</p>
			{#if userInfo.role === 'ROLE_STUDENT' && new Date(project.startDate) >= new Date()} <!-- Date en dur -->
				<button class="smallButton" on:click={() => addStudentInTeam(project.id)}>
					Rejoindre
				</button>
			{/if}
			{#if userInfo.role === 'ROLE_TEAM_MEMBER' && userInfo.idTeam === project.id && new Date(project.startDate) >= new Date()} <!-- Date en dur -->
				<button class="smallButton" on:click={() => removeStudentInTeam(project.id)}>
					Quitter
				</button>
			{/if}
			<a class="smallLink" href="/project/{project.id}" id="seeProject{project.id}">Voir l'équipe</a>
		</div>
	{/each}
</div>

<style lang="scss">
	.project-list {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: center;
		gap: 5%;
		color: black;
		margin-top: 50px;
	}
	.project-card {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		width: 400px;
		border-radius: 30px;
		background-color: white;
		color: black;
		padding: 2% 5% 2% 5%;
		gap: 10px;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		margin-bottom: 5%;
	}
	.date-project-card  {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		border-radius: 50px;
		background-color: white;
		color: black;
		padding: 2% 10% 4% 10%;
		margin: 50px 10% 3% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);

		.name-status {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			gap: 15px;

			input {
				width: 100%;
				height: 40px;
				border-radius: 10px;
				border: 1px solid #c4c4c4;
				padding: 10px;
			}
		}
	}
	
	.smallButton {
		padding: 10px;
		text-align: center;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
	}

	.smallButton:hover {
		cursor: pointer;
		background-color: #2b5ffb;
		transform: translateY(-2px);
	}

	.smallLink {
		padding: 10px 10px;
		text-align: center;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
		text-decoration: none;
	}

	.smallLink:hover {
		cursor: pointer;
		background-color: #2b5ffb;
		transform: translateY(-2px);
	}
</style>
