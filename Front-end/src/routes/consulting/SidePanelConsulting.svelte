<script>
	export let event;
	export let headersList;
	export let isStudent;
	export let allTeachers;
	export let userInfo;
	export let urlAPI;

	import { createEventDispatcher } from 'svelte';
	const dispatch = createEventDispatcher();

	let textInInputReview = event.review;
	let teacherAssignSelected;
	$: inputReviewDisabled = true;
	$: oldReview = '';
	$: teacherIsAvailable =
		(userInfo.role === 'ROLE_TEAM_MEMBER' || userInfo.role === 'ROLE_OPTION_LEADER') &&
		event.teachersId.includes(userInfo.userId);

	async function updateReview() {
		inputReviewDisabled = true;
		let body = JSON.stringify({
			idConsulting: event.idConsulting,
			review: textInInputReview
		});
		await fetch(urlAPI + '/teacher/updateReview', {
			method: 'PUT',
			headers: headersList,
			body: body
		});
	}

	async function assignTeacher() {
		let body = JSON.stringify({
			idConsulting: event.idConsulting,
			idTeacher: teacherAssignSelected
		});
		await fetch(urlAPI + '/consulting/assignTeacher', {
			method: 'PUT',
			headers: headersList,
			body: body
		});
		dispatch('assignTeacher');
	}

	function formatDate(date) {
		const options = {
			weekday: 'long',
			year: 'numeric',
			month: 'long',
			day: 'numeric',
			hour: 'numeric',
			minute: 'numeric'
		};
		const startDateInFrench = new Date(date).toLocaleDateString('fr-FR', options);
		return startDateInFrench;
	}
	function compareDate(today, endDate) {
		today.setDate(today.getDate() + 1);
		let now = new Date(today);
		let end = new Date(endDate);
		return end < now ? true : false;
	}

	//Ajout des disponibilités pour les consultings d'un teacher
	async function addDisponibility() {
		await fetch(urlAPI + `/teacher/addDisponibility`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				idPlanning: event.idPlanning,
				teacherId: userInfo.userId
			})
		});
		dispatch('addDispo');
		event.teachersId.push(userInfo.userId);
		teacherIsAvailable = true;
	}

	async function removeDisponibility(userId) {
		await fetch(urlAPI + `/teacher/removeDisponibility`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				idPlanning: event.idPlanning,
				teacherId: userId
			})
		});
		dispatch('removeDispo');
		event.teachersId.splice(event.teachersId.indexOf(userInfo.userId), 1);
		teacherIsAvailable = false;
	}

	async function askConsulting(specialityName) {
		await fetch(urlAPI + `/consulting/askConsulting`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				idTeam: userInfo.idTeam,
				idPlanning: event.idPlanning,
				speciality: specialityName
			})
		});
		addNotificationAskConsulting(specialityName);
		dispatch('askConsulting');
	}

	async function addNotificationAskConsulting(specialityName) {
		await fetch(urlAPI + `/notification/addNotification`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				personneSend: userInfo.userId,
				speciality: specialityName,
				message:
					'Demande de consulting en ' +
					specialityName +
					' ( Le ' +
					formatDate(event.dateStart) +
					' )',
				idPlanning: event.idPlanning
			})
		});
	}
</script>

<div class="container" id="info-consulting">
	{#if event.isConsulting}
		<h1>Informations du consulting</h1>
		<p>
			Spécialité :
			{#if event.title === 'MOD'}
				Modélisation
			{:else if event.title === 'INF'}
				Infrastructure
			{:else}
				Développement
			{/if}
		</p>
	{:else}
		<h1>Informations du créneau</h1>
	{/if}

	<p>Début : <br /> {formatDate(event.dateStart)}</p>
	<p>Durée : {event.slotDuration}</p>

	{#if event.isConsulting}
		{#if !isStudent}
			<div class="review">
				{#if inputReviewDisabled}
					{#if textInInputReview === '' || textInInputReview === null}
						{#if event.isAssigned}
							<h2>Review :</h2>
							<p>Pas de review disponible</p>
						{:else}
							<h3>En attente de validation ...</h3>
						{/if}
					{:else}
						<h2>Review :</h2>
						<p>{textInInputReview}</p>
					{/if}
				{:else}
					<h2>Review :</h2>
					<input type="text" bind:value={textInInputReview} placeholder="Entrez votre review" />
				{/if}
				{#if !isStudent}
					{#if inputReviewDisabled}
						<button
							class="smallButton"
							id="modify-review"
							on:click={() => {
								inputReviewDisabled = false;
								oldReview = textInInputReview;
							}}>Modifier</button
						>
					{:else}
						<div class="btn-group">
							<button class="smallButton" on:click={updateReview}>Valider</button>
							<button
								class="smallButton"
								on:click={() => {
									inputReviewDisabled = true;
									textInInputReview = oldReview;
								}}>Annuler</button
							>
						</div>
					{/if}
				{/if}
			</div>
		{/if}
		{#if !isStudent}
			{#if !event.isAssigned}
				<h2>Assigner à un professeur</h2>
				<div id="buttons-accept-refuse-consulting">
					<select id="select-teacher" bind:value={teacherAssignSelected}>
						<option disabled>-- Choisissez un professeur --</option>
						{#each allTeachers as teacher}
							<option value={teacher.id}>{teacher.name} {teacher.surname}</option>
						{/each}
					</select>
					<div class="btn-group">
						<button on:click={assignTeacher}>Accepter</button>
						<button>Refuser</button>
					</div>
				</div>
			{/if}
		{/if}
	{:else if !isStudent}
		{#if userInfo.role !== 'ROLE_PLANNING_ASSISTANT'}
		<div class="select-dispo-planning">
			<h2>Modifier mes disponibilités</h2>
			{#if teacherIsAvailable}
				<button class="smallButton" on:click={() => removeDisponibility(userInfo.userId)}
					>Je ne suis plus disponible</button
				>
			{:else}
				<button class="smallButton" on:click={addDisponibility}>Je suis disponible</button>
			{/if}
		</div>
		{/if}
	{:else}
		<p>
			Prof dispo sur ce créneau :
			{#each allTeachers as teacher}
				<li>{teacher.name} {teacher.surname}</li>
			{/each}
		</p>
		<button
			class="smallButton"
			id="ask-consulting-button"
			disabled={compareDate(new Date(), event.dateStart)}
			on:click={() => askConsulting(event.speciality)}>Demander le consulting</button
		>
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
		gap: 25px;
		border-radius: 15px;

		.select-dispo-planning {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			gap: 15px;
		}

		.review {
			width: 100%;
			display: flex;
			flex-direction: column;
			gap: 10px;

			input {
				padding: 10px;
				border-radius: 10px;
				width: 100%;
				border: 1px solid #c4c4c4;
			}
		}

		.btn-group {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: center;
			gap: 15px;
			align-self: center;
			width: 80%;
		}

		#buttons-accept-refuse-consulting {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			gap: 10px;
			width: 100%;

			#select-teacher {
				padding: 10px;
				border-radius: 10px;
				width: 100%;
			}

			.btn-group {
				display: flex;
				flex-direction: row;
				align-items: center;
				justify-content: center;
				gap: 15px;
				align-self: center;
				width: 80%;

				button {
					padding: 10px;
					width: 100%;
					text-align: center;
					border-radius: 10px;
					color: white;
					background-color: #3366ff;
					border: none;
					font-size: 1em;
				}
			}
		}
	}

	#modify-review {
		align-self: center;
	}

	.smallButton {
		padding: 10px;
		text-align: center;
		width: 80%;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
	}

	.smallButton:disabled {
		background-color: #cccccc;
		color: #666666;
		cursor: not-allowed;
	}
</style>
