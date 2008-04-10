import javax.imageio.*

class BootStrap {
	AuthenticateService authenticateService

    def init = { servletContext ->

        def adminRole = new Role(authority : 'ROLE_ADMIN', description : 'administrater')
        adminRole.save()
        def userRole = new Role(authority : 'ROLE_USER', description : 'user')
        userRole.save()

        def trungsi = new User(username : 'trungsi', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'admin')
        trungsi.save()

        adminRole.addToPeople(trungsi)
        userRole.addToPeople(trungsi)

        new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/login/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/album/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/photo/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/album/**",configAttribute:"ROLE_USER,ROLE_ADMIN").save()
        new Requestmap(url:"/photo/**",configAttribute:"ROLE_USER,ROLE_ADMIN").save()

        new File(System.getProperty('user.home') + '/image').listFiles().each {file ->
         		if (file.isFile())
         			createPhoto(file, trungsi)

     	 }
     }

     def destroy = {
     }

	def createPhoto = {file, owner ->
        //println file
        Photo.withTransaction {tx ->
	        def photo = new Photo(user : owner, description: file.name,
	                photoStream: file.newInputStream())

	        photo.save(flush : true)

	        if(! (photo.width != 0 && photo.height != 0)) {
	        	tx.setRollbackOnly()
	        }
        }
    }
}