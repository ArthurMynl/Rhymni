<script>
	export let data;
	import { goto } from '$app/navigation';
	import MarkCard from '$lib/MarkCard.svelte';
	import Navbar from '../../Navbar.svelte';

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}
	$: student = data.student;
	$: team = data.team;
	$: userInfo = data.userInfo;
	$: studentMark = data.student.studentMark;
	$: optionLeaderBonus = data.student.optionLeaderBonus;
	$: juryBonus = data.student.juryBonus;
	$: markPresentation = data.team.markPresentation;
	$: markValidation = data.team.markValidation;

	$: totalMark = studentMark + optionLeaderBonus + juryBonus + markPresentation + markValidation;
</script>

<Navbar {userInfo} />
<div class="container">
	<h1>{student.name} {student.surname}</h1>
	<div class="note-panel">
		<MarkCard
			mark={studentMark}
			maxMark={10}
			text="Note individuelle"
			canModify={userInfo.role === 'ROLE_JURY_MEMBER'}
			totalMark={totalMark}
			isBonus={false}
			noteType="studentMark"
			idStudent={student.id}
		/>
		<MarkCard
			mark={markPresentation}
			maxMark={5}
			text="Note de l'équipe"
			canModify={userInfo.role === 'ROLE_JURY_MEMBER'}
			totalMark={totalMark}
			isBonus={false}
			noteType="markPresentation"
			idTeam={team.number}
		/>
		<MarkCard
			mark={markValidation}
			maxMark={5}
			text="Note des rapports"
			canModify={userInfo.role === 'ROLE_JURY_MEMBER'}
			totalMark={totalMark}
			isBonus={false}
			noteType="markValidation"
			idTeam={team.number}
		/>
		<MarkCard
			mark={optionLeaderBonus}
			maxMark={20}
			text="Bonus du responsable d'option"
			canModify={userInfo.role === 'ROLE_OPTION_LEADER'}
			totalMark={totalMark}
			isBonus={true}
			noteType="optionLeaderBonus"
			idStudent={student.id}
		/>
		<MarkCard
			mark={juryBonus}
			maxMark={20}
			text="Bonus du jury"
			canModify={userInfo.role === 'ROLE_JURY_MEMBER'}
			totalMark={totalMark}
			isBonus={true}
			noteType="juryBonus"
			idStudent={student.id}
		/>
		<MarkCard
			mark={totalMark}
			maxMark={20}
			text="Note finale"
			canModify={false}
			totalMark={totalMark}
			isBonus={false}
		/>
	</div>
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;

		.note-panel {
			display: grid;
			grid-template-columns: repeat(2, 1fr);
			grid-template-rows: repeat(3, 1fr);
			grid-gap: 25px; /* Adjust the gap between grid items as needed */
			width: 80%;
			margin: 0 auto;
			padding: 40px 60px;
			border-radius: 20px;
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		}
	}
</style>
