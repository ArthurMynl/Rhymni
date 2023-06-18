<script>
	export let data;
	import TeacherButton from './TeacherButton.svelte';
	import Navbar from '../Navbar.svelte';
	let teachers = data.teachers;
	let userInfo = data.userInfo;

	import { goto } from '$app/navigation';

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}
</script>

<Navbar {userInfo}/>
<div class="list-teachers">
	{#each teachers as teacher}
		<TeacherButton {teacher} />
	{/each}
</div>

<style>
	.list-teachers {
		display: flex;
		flex-direction: row;
		justify-content: center;
		flex-wrap: wrap;
		align-items: center;
		gap: 70px;
		margin: 50px 0;
	}
</style>
