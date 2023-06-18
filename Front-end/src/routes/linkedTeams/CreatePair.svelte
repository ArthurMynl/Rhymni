<script>
	export let teams;
	let selectTeam1;
	let selectTeam2;
	import Cookies from 'js-cookie';
	import { createEventDispatcher } from 'svelte';
	import { toast } from '@zerodevx/svelte-toast';
	const dispatch = createEventDispatcher();

	$: isSameTeam = selectTeam1 === selectTeam2;

	async function postPair() {
		const urlAPI = process.env.URLAPI;
		const headersList = {
			'Content-Type': 'application/json;charset=UTF-8',
			Authorization: 'Bearer ' + Cookies.get('token')
		};
		const formData = {
			idLinkedTeam1: selectTeam1,
			idLinkedTeam2: selectTeam2
		};
		let response = await fetch(urlAPI + '/optionLeader/createTeamPair', {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify(formData)
		});

		toast.push("La paire d'équipe a été crée !", {
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#00a66b',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});

		if (response.ok) {
			dispatch('pairCreated');
		}
		
	}
</script>

<div class="create-pair">
	<select class="select-team" id="teamInput1" bind:value={selectTeam1}>
		<option disabled>-- Choisissez une équipe --</option>
		{#each teams as team}
			<option value={team.number}>Team n°{team.number}</option>
		{/each}
	</select>
	-
	<select class="select-team" id="teamInput2" bind:value={selectTeam2}>
		<option disabled>-- Choisissez une équipe --</option>
		{#each teams as team}
			<option value={team.number}>Team n°{team.number}</option>
		{/each}
	</select>
</div>

<button class="smallButton" id="buttonCreate" disabled={isSameTeam} on:click={postPair}>Créer la paire</button>

<!-- <p id="textAlert" hidden="true">Vous ne pouvez pas lier une équipe avec elle-même</p> -->

<style>
	.create-pair {
		display: flex;
		flex-direction: row;
		justify-content: center;
		gap: 15px;
		align-items: center;
		margin-bottom: 20px;
		width: 100%;
	}

	.smallButton {
		padding: 3% 5%;
		text-align: center;
		border-radius: 10px;
		width: 50%;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
		cursor: pointer;
	}

	.smallButton:disabled {
		background-color: #cccccc;
		color: #666666;
		cursor: not-allowed;
	}

	.select-team {
		padding: 1% 0;
		text-align: center;
		border-radius: 10px;
	}
</style>
