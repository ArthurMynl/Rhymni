<script>
	const urlAPI = process.env.URLAPI;
	let type, date, hours, idRoom, idTeacher, idTeam;
	import Cookies from 'js-cookie';
	import { toast } from '@zerodevx/svelte-toast';
	export let rooms;
	export let teachers;
	export let teams;

	function createPresentation() {
		if (type == 'intermediaire') {
			fetch(urlAPI + `/planning/createIntermediatePresentation`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: 'Bearer ' + Cookies.get('token')
				},
				body: JSON.stringify({
					date: date,
					hours: hours,
					idRoom: idRoom.numberRoom,
					idTeacher: idTeacher.id,
					idTeam: idTeam.number
				})
			})
				.then((response) => {
					if (response.status === 201) {
						window.location.reload();
					} else if (response.status === 400) {
						throw new Error('Le professeur a déjà un consulting à cette date.');
					} else if (response.status === 409) {
						throw new Error("L'équipe à déjà une soutenance intermédiaire.");
					} else {
						throw new Error("Une erreur inattendue s'est produite.");
					}
				})
				.catch((error) => {
					toast.push(error.message, {
						theme: {
							'--toastBackground': '#ff0000',
							'--toastProgressBackground': '#ff0000',
							'--toastProgressAfterBackground': '#ff0000',
							'--toastIconFill': '#fff',
							'--toastBorderRadius': '15px'
						},
						pausable: true
					});
				});
		} else if (type == 'finale') {
			fetch(urlAPI + `/planning/createFinalPresentation`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: 'Bearer ' + Cookies.get('token')
				},
				body: JSON.stringify({
					date: date,
					hours: hours,
					idRoom: idRoom.numberRoom,
					idTeacher: idTeacher.id,
					idTeam: idTeam.number
				})
			})
				.then((response) => {
					if (response.status === 201 || response.status === 200) {
						window.location.reload();
					} else if (response.status === 400) {
						throw new Error('Le professeur a déjà une soutenance à cette date.');
					} else if (response.status === 409) {
						throw new Error("L'équipe à déjà une soutenance finale.");
					} else if (response.status === 502) {
						throw new Error("L'équipe n'à pas de soutenance intermédiaire.");
					} else if (response.status === 417) {
						throw new Error('Une soutenance finale doit être après la soutenance intermédiaire.');
					} else {
						throw new Error("Une erreur inattendue s'est produite.");
					}
				})
				.catch((error) => {
					toast.push(error.message, {
						theme: {
							'--toastBackground': '#ff0000',
							'--toastProgressBackground': '#ff0000',
							'--toastProgressAfterBackground': '#ff0000',
							'--toastIconFill': '#fff',
							'--toastBorderRadius': '15px'
						},
						pausable: true
					});
				});
		}
	}

	let displayedTeams = [];

	async function updateDisplayedTeams(teamNumber) {
		displayedTeams.push(teamNumber);
		displayedTeams.push(teamNumber.linkedTeamNumber);
	}
</script>

<div class="container" id="presentationCreate">
	<h1>Créer une soutenance</h1>
	<div class="create-presentation" id="presentationForm">
		<div class="group">
			<select bind:value={type} id="presentationType">
				<option value="intermediaire">Soutenance intermédiaire</option>
				<option value="finale">Soutenance finale</option>
			</select>
			<select bind:value={idTeam} id="presentationTeam">
				{#each teams as team}
					{#if !displayedTeams.includes(team.number)}
						<option value={team}>
							Équipe {team.number} - Équipe {team.linkedTeamNumber}
						</option>
						{#if team.linkedTeamNumber !== team.number}
							{#if !displayedTeams.includes(team.linkedTeamNumber)}
								{updateDisplayedTeams(team.linkedTeamNumber)}
							{/if}
						{/if}
					{/if}
				{/each}
			</select>
		</div>
		<div class="group">
			<input type="date" bind:value={date} id="presentationDate"/>
			<input type="time" bind:value={hours} id = presentationTime/>
		</div>
		<div class="group">
			<select bind:value={idRoom} id="presentationRoom">
				{#each rooms as room}
					<option value={room}>
						{room.name}
					</option>
				{/each}
			</select>
			<select bind:value={idTeacher} id="presentationTeacher">
				{#each teachers as teacher}
					<option value={teacher[0]}>
						{teacher[0].surname}
						{teacher[0].name} -
						{teacher[1].surname}
						{teacher[1].name}
					</option>
				{/each}
			</select>
		</div>
	</div>
	<button class="smallButton" id="presentationAjouter" on:click={createPresentation}>Ajouter</button>
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		border-radius: 50px;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		width: 70%;
		margin-bottom: 60px;
		gap: 30px;
		padding: 30px 0;

		.smallButton {
			padding: 10px;
			text-align: center;
			border-radius: 10px;
			width: 60%;
			color: white;
			background-color: #3366ff;
			border: none;
			font-size: 1em;
		}

		.create-presentation {
			display: flex;
			flex-direction: column;
			justify-content: center;
			width: 60%;
			gap: 10px;

			.group {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				width: 100%;
				gap: 10px;

				input,
				select {
					width: 100%;
					height: 40px;
					border-radius: 10px;
					border: 1px solid #c4c4c4;
					padding: 10px;
				}
			}
		}
	}
</style>
