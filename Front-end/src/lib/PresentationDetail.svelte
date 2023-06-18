<script>
	export let presentation;
	export let userInfo;

	import Mark from './Mark.svelte';
	import PresentationTeamInfo from './PresentationTeamInfo.svelte';

	function formatDateTime(originalDate) {
		if (!(originalDate instanceof Date)) {
			originalDate = new Date(originalDate);
		}

		const formattedDate = originalDate.toLocaleDateString('fr-FR', {
			day: 'numeric',
			month: 'long',
			year: 'numeric'
		});

		const formattedTime = originalDate.toLocaleTimeString('fr-FR', {
			hour: 'numeric',
			minute: 'numeric'
		});

		const formattedDateTime = `${formattedDate} à ${formattedTime}`;

		return formattedDateTime;
	}

	console.log(presentation);
</script>

<div class="container">
	<div class="global-info-presentation">
		<div class="title">
			<h3>Équipe n°{presentation.teams[0].number} - Équipe n°{presentation.teams[1].number}</h3>
			<p>{presentation.type}</p>
		</div>
		<div class="content">
			<div>
				<h3>Date</h3>
				<p><span id="formattedDate">{formatDateTime(presentation.dateHours)}</span></p>
			</div>
			<div>
				<h3>Professeurs</h3>
				<p>
					{presentation.teacher[0].name}
					{presentation.teacher[0].surname} - {presentation.teacher[1].name}
					{presentation.teacher[1].surname}
				</p>
			</div>
			<div>
				<h3>Salle</h3>
				<p>{presentation.room.name} n°{presentation.room.numberRoom}</p>
			</div>
		</div>
	</div>
	{#if userInfo.idTeam == presentation.teams[0].number || userInfo.idTeam == presentation.teams[1].number || userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER' || userInfo.role === 'ROLE_OPTION_LEADER' || userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
		<hr />
		<div class="info-presentation-teams">
			<div class="info-team1">
				<PresentationTeamInfo {presentation} {userInfo} team0or1={0} />
			</div>
			<div class="info-team2">
				<PresentationTeamInfo {presentation} {userInfo} team0or1={1} />
			</div>
		</div>
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 30px;
		width: 100%;

		.global-info-presentation {
			display: flex;
			flex-direction: column;
			width: 100%;
			gap: 30px;

			.title {
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				align-items: center;
				width: 100%;
			}

			.content {
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;
				width: 100%;
			}
		}

		hr {
			width: 80%;
		}

		.info-presentation-teams {
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			width: 100%;
		}
	}
</style>
