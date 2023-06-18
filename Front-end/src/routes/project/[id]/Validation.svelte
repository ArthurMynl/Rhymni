<script>
	const urlAPI = process.env.URLAPI;
	import { onMount } from 'svelte';
	export let project;
	import Cookies from 'js-cookie';
	import { toast } from '@zerodevx/svelte-toast';
	let project_id = project.id;
	$: status = project.status;
	let teachers = [];
	let teacher_id = null;
	export let userInfo;
	$: modeRefuseProject = false;
	let rejectComment = project.rejectComment;

	function changeModeRefuseProject() {
		modeRefuseProject = !modeRefuseProject;
	}

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	async function loadTeachers() {
		const response = await fetch(urlAPI + '/teacher/getTeacher', {
			method: 'GET',
			headers: headersList
		});
		const data = await response.json();
		teachers = data;
	}

	$: hasRightsToValidate = false;
	async function getValidationRights() {
		const response = await fetch(urlAPI + `/project/getValidationRights/${project_id}`, {
			method: 'GET',
			headers: headersList
		});
		const data = await response.json();
		return data.includes(userInfo.userId) || userInfo.role === 'ROLE_OPTION_LEADER';
	}

	onMount(async () => {
		loadTeachers();
		hasRightsToValidate = await getValidationRights();
	});

	function validateProject() {
		fetch(urlAPI + `/project/changeStatus`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				id: project_id,
				validate: true
			})
		});
		status = 'VALIDATED';
	}
	function refuseProject() {
		fetch(urlAPI + `/project/changeStatus`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				id: project_id,
				validate: false,
				rejectComment
			})
		});
		status = 'REFUSED';
		project.rejectComment = rejectComment;	
		changeModeRefuseProject();
		//updateRejectComment();
	}

	function delegateProject(teacher_id) {
		fetch(urlAPI + `/optionLeader/delegateValidationProject`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				idTeacher: teacher_id,
				idProject: project_id
			})
		});
		status = 'PENDING';
		toast.push('La validation du projet a bien été délégué !', {
			theme: {
				'--toastBackground': '#3366ff',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}
</script>

<div class="project-validation">	
	{#if hasRightsToValidate === true && !modeRefuseProject}
		{#if status === 'VALIDATED'}
			<div class="validation-group" id="TrueValidation">
				<h4>Projet validé</h4>			
				<button on:click={changeModeRefuseProject} class="validation-button" id ="refuseSubject">Refuser</button>
			</div>
		{:else if status === 'REFUSED'}
			<div class="validation-group" id="TrueRefuse">
				<h4>Projet refusé</h4>
				<p>raison du refus : </p>
				{project.rejectComment}				
				<button on:click={validateProject} class="validation-button" id="validateSubject">Valider</button>
			</div>
		{:else}
			<p>Statut : En attente de validation</p>
			<div class="validation-group">
				<button on:click={validateProject} class="validation-button" id="validateSubject">Valider</button>
				<button on:click={changeModeRefuseProject} class="validation-button" id="refuseSubject">Refuser</button>
			</div>
			{#if userInfo.role === 'ROLE_OPTION_LEADER'}
				<div class="delegate-validation">
					<select bind:value={teacher_id}>
						<option value={null} disabled selected>Choisir un enseignant</option>
						{#each teachers as teacher (teacher.id)}
							<option value={teacher.id}>{teacher.name} {teacher.surname}</option>
						{/each}
					</select>
					<button
						on:click={() => delegateProject(teacher_id)}
						class="validation-button"
						disabled={!teacher_id}
					>
						Déléguer la validation
					</button>					
				</div>
			{/if}			
		{/if}
	{:else if !modeRefuseProject}				
		{#if status === 'VALIDATED'}
			<div class="validation-group" id="TrueValidation">
				<h4>Projet validé</h4>
			</div>
		{:else if status === 'REFUSED'}
			<div class="validation-group" id="TrueRefuse">
				<h4>Projet refusé</h4>
				<p>raison du refus : </p>
				{project.rejectComment}				
			</div>
		{:else}
			<p>Projet : En attente de validation</p>
		{/if}
	{/if}	
	{#if modeRefuseProject}
		<div class="refuse-group">
			<input bind:value={rejectComment} class="input-text" type="text" placeholder= "raison du refus"/>
			{#if rejectComment}
				<button on:click={refuseProject} class="validation-button">Valider</button>
			{:else}
				<button class="validation-button-blocked">Valider</button>
			{/if}
			<button on:click={changeModeRefuseProject} class="Annulation-button">Annuler</button>
		</div>
	{/if}
</div>

<style lang="scss">
	.project-validation {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 10px;

		.validation-group {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			gap: 10px;

			.validation-button {
				background-color: #3366ff;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
				font-size: 1em;

				&:hover {
					cursor: pointer;
					background-color: #1a53ff;
				}
			}
		}
		.refuse-group {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: flex-start;
			gap: 5px;
			width: 100%;

			input {
				padding: 10px;
				border-radius: 10px;
				width: 100%;
				border: 1px solid #c4c4c4;
			}

			.validation-button-blocked{
				background-color: #989898;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
				font-size: 1em;
			}
			.validation-button {
				background-color: #3366ff;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
				font-size: 1em;
				&:hover {
					cursor: pointer;
					background-color: #1a53ff;
				}
			}
			.Annulation-button {
				background-color: #ea735c;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
				font-size: 1em;
				&:hover {
					cursor: pointer;
					background-color: #ea735c;
				}
			}
		}

		.delegate-validation {
			display: flex;
			flex-direction: column;

			select {
				border: none;
				border-radius: 10px;
				padding: 10px;
				border: 1px solid #c4c4c4;
				font-size: 1rem;
				font-weight: 500;
				width: 100%;
				margin-bottom: 10px;
			}
	
			button {
				background-color: #3366ff;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
	
				&:hover {
					cursor: pointer;
					background-color: #1a53ff;
				}
			}
		}
	}
</style>
