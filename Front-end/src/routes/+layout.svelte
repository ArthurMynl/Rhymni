<script>
	import '../app.scss';
	import Footer from './Footer.svelte';
	import Navbar from './Navbar.svelte';
	import { SvelteToast } from '@zerodevx/svelte-toast'
</script>

<div class="container">	
	<SvelteToast />
	<slot />
	<Footer />
</div>

<style>
	.container {
		background-color: #ffffff;
		min-height: 100vh;
		min-width: 100vw;
		position: absolute;
		top: 0;
		left: 0;
		font-family: "Lato", 'sans-serif';
	}
</style>
