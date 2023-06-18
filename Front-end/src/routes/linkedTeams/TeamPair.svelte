<script>
	const urlAPI = process.env.URLAPI;
	export let team1;
	export let team2;
	import Cookies from 'js-cookie';
	import { toast } from '@zerodevx/svelte-toast';
	export let userInfo;

	import { createEventDispatcher } from 'svelte';
	const dispatch = createEventDispatcher();

	async function deletePair() {
		const headersList = {
			'Content-Type': 'application/json;charset=UTF-8',
			Authorization: 'Bearer ' + Cookies.get('token')
		};
		const formData = {
			idLinkedTeam1: team1,
			idLinkedTeam2: team2
		};
		let response = await fetch(urlAPI + '/optionLeader/deleteTeamPair', {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify(formData)
		});

		toast.push("La paire d'équipe a été supprimée.",{
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#eb4e3f',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});

		if (response.ok) {
			dispatch('pairDeleted');
		}
		
	}
</script>

<div class="team-pair">
	<p>Team {team1} - Team {team2}</p>
	{#if userInfo.role === 'ROLE_OPTION_LEADER'}
		<button class="smallButton" on:click={deletePair}>Supprimer</button>
	{/if}
</div>

<style>
	.team-pair {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		gap: 2rem;
		width: 100%;
	}

	.smallButton {
		padding: 2% 3%;
		text-align: center;
		border-radius: 10px;
		width: 30%;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
	}
</style>
